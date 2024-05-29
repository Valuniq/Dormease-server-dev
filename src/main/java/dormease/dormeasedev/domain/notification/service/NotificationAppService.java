package dormease.dormeasedev.domain.notification.service;

import dormease.dormeasedev.domain.block.domain.Block;
import dormease.dormeasedev.domain.block.domain.repository.BlockRepository;
import dormease.dormeasedev.domain.block.dto.response.BlockRes;
import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.file.domain.repository.FileRepository;
import dormease.dormeasedev.domain.file.dto.response.FileRes;
import dormease.dormeasedev.domain.notification.domain.Notification;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notification.domain.repository.NotificationRepository;
import dormease.dormeasedev.domain.notification.dto.response.NotificationAppRes;
import dormease.dormeasedev.domain.notification.dto.response.NotificationDetailAppRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationAppService {

    private final NotificationRepository notificationRepository;
    private final BlockRepository blockRepository;
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

        // TODO :
        User user = userService.validateUserById(customUserDetails.getId());
        Notification notification = notificationWebService.validateById(notificationId);

        DefaultAssert.isTrue(user.getSchool().equals(notification.getSchool()), "해당 학생의 학교만 조회할 수 있습니다.");

        List<Block> blockList = blockRepository.findByNotification(notification);
        List<BlockRes> blockResList = new ArrayList<>();
        for (Block block : blockList) {
            BlockRes blockRes = BlockRes.builder()
                    .blockId(block.getId())
                    .imageUrl(block.getImageUrl())
                    .sequence(block.getSequence())
                    .content(block.getContent())
                    .build();
            blockResList.add(blockRes);
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
                .blockResList(blockResList)
                .fileList(fileResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notificationDetailAppRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
