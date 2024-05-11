package dormease.dormeasedev.domain.notification.service;

import dormease.dormeasedev.domain.block.domain.Block;
import dormease.dormeasedev.domain.block.domain.repository.BlockRepository;
import dormease.dormeasedev.domain.block.dto.request.BlockReq;
import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.file.domain.repository.FileRepository;
import dormease.dormeasedev.domain.notification.domain.Notification;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notification.domain.repository.NotificationRepository;
import dormease.dormeasedev.domain.notification.dto.request.WriteNotificataionReq;
import dormease.dormeasedev.domain.notification.dto.response.NotificationRes;
import dormease.dormeasedev.domain.s3.service.S3Uploader;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationWebService {

    private final NotificationRepository notificationRepository;
    private final BlockRepository blockRepository;
    private final FileRepository fileRepository;

    private final UserService userService;

    private final S3Uploader s3Uploader;

    // Description : 공지사항(FAQ) 목록 조회
    public ResponseEntity<?> findNotifications(CustomUserDetails customUserDetails, NotificationType notificationType, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 23, Sort.by(Sort.Order.desc("pinned"), Sort.Order.asc("createdDate")));
        Page<Notification> notificationPage = notificationRepository.findNotificationsBySchoolAndNotificationType(school, notificationType, pageable);
        List<Notification> notificationList = notificationPage.getContent();

        List<NotificationRes> notificationResList = new ArrayList<>();
        for (Notification notification : notificationList) {
            boolean existFile = fileRepository.existsByNotification(notification);
            NotificationRes notificationRes = NotificationRes.builder()
                    .pinned(notification.getPinned())
                    .title(notification.getTitle())
                    .writer(notification.getWriter())
                    .createdDate(notification.getCreatedDate().toLocalDate())
                    .existFile(existFile)
                    .build();
            notificationResList.add(notificationRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, notificationPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, notificationResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 작성
    @Transactional
    public ResponseEntity<?> writeNotification(CustomUserDetails customUserDetails, WriteNotificataionReq writeNotificataionReq, List<MultipartFile> multipartFiles) {

        User admin = userService.validateUserById(customUserDetails.getId());
            School school = admin.getSchool();

        Notification notification = Notification.builder()
                .school(school)
                .notificationType(writeNotificataionReq.getNotificationType())
                .title(writeNotificataionReq.getTitle())
                .pinned(writeNotificataionReq.getPinned())
                .writer(admin.getName())
                .build();
        notificationRepository.save(notification);

        List<BlockReq> blockReqList = writeNotificataionReq.getBlockReqList();
        for (BlockReq blockReq : blockReqList) {
            Block block = Block.builder()
                    .notification(notification)
                    .imageUrl(blockReq.getImageUrl())
                    .sequence(blockReq.getSequence())
                    .content(blockReq.getContent())
                    .build();
            blockRepository.save(block);
        }

        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl;
            if (multipartFile.isEmpty())
                fileUrl = null;
            else
                fileUrl = s3Uploader.uploadImage(multipartFile);

            File file = File.builder()
                    .notification(notification)
                    .fileUrl(fileUrl)
                    .build();

            fileRepository.save(file);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("공지사항(혹은 FAQ) 등록이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
