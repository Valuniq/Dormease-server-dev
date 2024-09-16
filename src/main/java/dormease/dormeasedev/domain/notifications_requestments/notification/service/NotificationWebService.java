package dormease.dormeasedev.domain.notifications_requestments.notification.service;

import dormease.dormeasedev.domain.notifications_requestments.file.domain.File;
import dormease.dormeasedev.domain.notifications_requestments.file.domain.repository.FileRepository;
import dormease.dormeasedev.domain.notifications_requestments.file.dto.response.FileRes;
import dormease.dormeasedev.domain.notifications_requestments.image.domain.Image;
import dormease.dormeasedev.domain.notifications_requestments.image.domain.repository.ImageRepository;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.request.ImageReq;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.response.ImageRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.Notification;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.repository.NotificationRepository;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.request.ModifyNotificationReq;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.request.WriteNotificationReq;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationDetailWebRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationWebRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageInfo;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.infrastructure.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationWebService {

    private final NotificationRepository notificationRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;

    private final UserService userService;

    private final S3Uploader s3Uploader;

    // Description : 공지사항(FAQ) 목록 조회
    public ResponseEntity<?> findNotifications(CustomUserDetails customUserDetails, NotificationType notificationType, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 23, Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("createdDate")));
        Page<Notification> notificationPage = notificationRepository.findNotificationsBySchoolAndNotificationType(school, notificationType, pageable);
        List<Notification> notificationList = notificationPage.getContent();

        List<NotificationWebRes> notificationWebResList = new ArrayList<>();
        for (Notification notification : notificationList) {
            boolean existFile = fileRepository.existsByNotification(notification);
            NotificationWebRes notificationWebRes = NotificationWebRes.builder()
                    .notificationId(notification.getId())
                    .pinned(notification.getPinned())
                    .title(notification.getTitle())
                    .writer(notification.getUser().getName())
                    .createdDate(notification.getCreatedDate().toLocalDate())
                    .existFile(existFile)
                    .build();
            notificationWebResList.add(notificationWebRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, notificationPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, notificationWebResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 작성
    @Transactional
    public ResponseEntity<?> writeNotification(CustomUserDetails customUserDetails, WriteNotificationReq writeNotificationReq, List<MultipartFile> multipartFiles) throws IOException {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Notification notification = Notification.builder()
                .school(school)
                .user(admin)
                .notificationType(writeNotificationReq.getNotificationType())
                .title(writeNotificationReq.getTitle())
                .pinned(writeNotificationReq.getPinned())
                .content(writeNotificationReq.getContent())
                .build();
        notificationRepository.save(notification);

        List<ImageReq> imageReqList = writeNotificationReq.getImageReqList();
        createImages(notification, imageReqList);
        if (multipartFiles != null)
            uploadFiles(notification, multipartFiles);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("공지사항(FAQ) 등록이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 상세 조회
    public ResponseEntity<?> findNotification(CustomUserDetails customUserDetails, Long notificationId) {

        User admin = userService.validateUserById(customUserDetails.getId());
        Notification notification = validateById(notificationId);

        DefaultAssert.isTrue(admin.getSchool().equals(notification.getSchool()), "해당 관리자의 학교만 조회할 수 있습니다.");

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

        NotificationDetailWebRes notificationDetailWebRes = NotificationDetailWebRes.builder()
                .pinned(notification.getPinned())
                .title(notification.getTitle())
                .writer(notification.getUser().getName())
                .createdDate(notification.getCreatedDate().toLocalDate())
                .modifiedDate(notification.getModifiedDate().toLocalDate())
                .content(notification.getContent())
                .imageResList(imageResList)
                .fileList(fileResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notificationDetailWebRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 수정
    @Transactional
    public ResponseEntity<?> modifyNotification(CustomUserDetails customUserDetails, ModifyNotificationReq modifyNotificationReq, List<MultipartFile> multipartFiles) throws IOException {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();
        Notification notification = validateById(modifyNotificationReq.getNotificationId());

        DefaultAssert.isTrue(admin.getSchool().equals(notification.getSchool()), "해당 관리자의 학교만 수정할 수 있습니다.");

        if (StringUtils.hasLength(modifyNotificationReq.getTitle()))
            notification.updateTitle(modifyNotificationReq.getTitle());

        if (StringUtils.hasLength(modifyNotificationReq.getContent()))
            notification.updateContent(modifyNotificationReq.getContent());

        if (modifyNotificationReq.getPinned() != null && notification.getPinned() != modifyNotificationReq.getPinned())
            notification.updatePinned();

        if (!modifyNotificationReq.getDeletedImageIds().isEmpty())
            deleteImages(modifyNotificationReq.getDeletedImageIds(), school);

        if (!modifyNotificationReq.getImageReqList().isEmpty())
            createImages(notification, modifyNotificationReq.getImageReqList());

        if (!modifyNotificationReq.getDeletedFileIds().isEmpty())
            deleteFiles(modifyNotificationReq.getDeletedFileIds(), school);

        if (multipartFiles != null)
            uploadFiles(notification, multipartFiles);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("공지사항(FAQ) 수정이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 삭제
    @Transactional
    public ResponseEntity<?> deleteNotification(CustomUserDetails customUserDetails, Long notificationId) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();
        Notification notification = validateById(notificationId);

        DefaultAssert.isTrue(school.equals(notification.getSchool()), "해당 관리자의 학교만 삭제할 수 있습니다.");

        deleteImages(notification, school);
        deleteFiles(notification, school);
        notificationRepository.delete(notification);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("공지사항(FAQ) 삭제가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // Description : 유효성 검증 함수
    public Notification validateById(Long notificationId) {
        Optional<Notification> findNotification = notificationRepository.findById(notificationId);
        DefaultAssert.isTrue(findNotification.isPresent(), "존재하지 않는 공지사항(FAQ)입니다.");
        return findNotification.get();
    }

    // Description : 파일 업로드 함수
    public void uploadFiles(Notification notification, List<MultipartFile> multipartFiles) throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl;
            if (multipartFile.isEmpty())
                continue;
            else
                fileUrl = s3Uploader.uploadImage(multipartFile);

            String originalFileName = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

            File file = File.builder()
                    .notification(notification)
                    .fileUrl(fileUrl)
                    .originalFileName(originalFileName)
                    .build();
            fileRepository.save(file);
            file.addNotification(notification);
        }
    }

    // Description : 작성(수정) 시 테이블에 저장 (s3는 작성(수정) 버튼 클릭 이전에 올림)
    public void createImages(Notification notification, List<ImageReq> imageReqList) {

        for (ImageReq imageReq : imageReqList) {
            Image image = Image.builder()
                    .notification(notification)
                    .imageUrl(imageReq.getImageUrl())
                    .build();
            imageRepository.save(image);
            image.addNotification(notification);
        }
    }

    // Description : s3에서 이미지 삭제 + image에서 삭제 (ids)
    public void deleteImages(List<Long> deleteImageIds, School school) {

        if (!deleteImageIds.isEmpty()) {
            List<Image> images = imageRepository.findAllById(deleteImageIds);
            // s3에서 삭제
            for (Image image : images) {
                DefaultAssert.isTrue(image.getNotification().getSchool().equals(school), "해당 관리자가 속한 학교의 이미지만 삭제할 수 있습니다.");
                String imageName = image.getImageUrl().split("amazonaws.com/")[1];
                s3Uploader.deleteFile(imageName);
            }
            imageRepository.deleteAll(images);
        }
    }

    // Description : s3에서 이미지 삭제 + image에서 삭제 (notification)
    public void deleteImages(Notification notification, School school) {
        List<Image> imageList = imageRepository.findByNotification(notification);
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                DefaultAssert.isTrue(image.getNotification().getSchool().equals(school), "해당 관리자가 속한 학교의 이미지만 삭제할 수 있습니다.");
                // s3에서 삭제
                String imageName = image.getImageUrl().split("amazonaws.com/")[1];
                s3Uploader.deleteFile(imageName);
            }
            imageRepository.deleteAll(imageList);
        }
    }

    // Description : s3에서 파일 삭제 + file에서 삭제 (ids)
    public void deleteFiles(List<Long> deleteFileIds, School school) {

        if (!deleteFileIds.isEmpty()) {
            List<File> files = fileRepository.findAllById(deleteFileIds);
            // s3에서 삭제
            for (File file : files) {
                DefaultAssert.isTrue(file.getNotification().getSchool().equals(school), "해당 관리자가 속한 학교의 파일만 삭제할 수 있습니다.");
                String fileName = file.getFileUrl().split("amazonaws.com/")[1];
                s3Uploader.deleteFile(fileName);
            }
            fileRepository.deleteAll(files);
        }
    }

    // Description : s3에서 파일 삭제 + file에서 삭제 (notification)
    public void deleteFiles(Notification notification, School school) {
        List<File> fileList = fileRepository.findByNotification(notification);
        if (!fileList.isEmpty()) {
            for (File file : fileList) {
                DefaultAssert.isTrue(file.getNotification().getSchool().equals(school), "해당 관리자가 속한 학교의 파일만 삭제할 수 있습니다.");
                // s3에서 삭제
                String fileName = file.getFileUrl().split("amazonaws.com/")[1];
                s3Uploader.deleteFile(fileName);
            }
            fileRepository.deleteAll(fileList);
        }
    }
}
