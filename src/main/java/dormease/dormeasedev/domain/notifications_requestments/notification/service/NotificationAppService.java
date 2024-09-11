package dormease.dormeasedev.domain.notifications_requestments.notification.service;

import dormease.dormeasedev.domain.notifications_requestments.file.domain.File;
import dormease.dormeasedev.domain.notifications_requestments.file.domain.repository.FileRepository;
import dormease.dormeasedev.domain.notifications_requestments.file.dto.response.FileRes;
import dormease.dormeasedev.domain.notifications_requestments.image.domain.Image;
import dormease.dormeasedev.domain.notifications_requestments.image.domain.repository.ImageRepository;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.response.ImageRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.Notification;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.repository.NotificationRepository;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationAppRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationDetailAppRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationMainRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationAppService {

    private final NotificationRepository notificationRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;

    private final UserService userService;
    private final NotificationWebService notificationWebService;

    // Description : 공지사항(FAQ) 목록 조회
    public ResponseEntity<?> findNotifications(CustomUserDetails customUserDetails, NotificationType notificationType, Integer page) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("createdDate")));
        Page<Notification> notificationPage = notificationRepository.findNotificationsBySchoolAndNotificationType(school, notificationType, pageable);
        List<Notification> notificationList = notificationPage.getContent();

        List<NotificationAppRes> notificationAppResList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationAppRes notificationAppRes = NotificationAppRes.builder()
                    .notificationId(notification.getId())
                    .pinned(notification.getPinned())
                    .title(notification.getTitle())
                    .createdDate(notification.getCreatedDate().toLocalDate())
                    .build();
            notificationAppResList.add(notificationAppRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, notificationPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, notificationAppResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 상세 조회
    public ResponseEntity<?> findNotification(CustomUserDetails customUserDetails, Long notificationId) {

        User user = userService.validateUserById(customUserDetails.getId());
        Notification notification = notificationWebService.validateById(notificationId);

        DefaultAssert.isTrue(user.getSchool().equals(notification.getSchool()), "해당 학생의 학교만 조회할 수 있습니다.");

        List<Image> imageList = imageRepository.findByNotification(notification);
        List<ImageRes> imageResList = new ArrayList<>();
        for (Image image : imageList) {
            ImageRes imageRes = ImageRes.builder()
                    .imageId(image.getId())
                    .imageUrl(image.getImageUrl())
                    .build();
            imageResList.add(imageRes);
        }

        List<File> fileList = fileRepository.findByNotification(notification);
        List<FileRes> fileResList = new ArrayList<>();
        for (File file : fileList) {
            FileRes fileRes = FileRes.builder()
                    .fileId(file.getId())
                    .fileUrl(file.getFileUrl())
                    .originalFileName(file.getOriginalFileName())
                    .build();
            fileResList.add(fileRes);
        }

        NotificationDetailAppRes notificationDetailAppRes = NotificationDetailAppRes.builder()
                .title(notification.getTitle())
                .createdDate(notification.getCreatedDate().toLocalDate())
                .content(notification.getContent())
                .imageResList(imageResList)
                .fileList(fileResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notificationDetailAppRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 메인 페이지 공지사항 조회 - 상단핀 고정된 제일 최근 공지사항
    public ResponseEntity<?> findMainNotification(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Optional<Notification> findNotification = notificationRepository.findTopBySchoolAndNotificationTypeAndPinnedOrderByCreatedDateDesc(school, NotificationType.ANNOUNCEMENT, true);
        DefaultAssert.isTrue(findNotification.isPresent(), "해당 학교에 상단핀 고정된 공지사항이 없습니다.");
        Notification notification = findNotification.get();

        NotificationMainRes notificationMainRes = NotificationMainRes.builder()
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .createdDate(notification.getModifiedDate().toLocalDate())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notificationMainRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
