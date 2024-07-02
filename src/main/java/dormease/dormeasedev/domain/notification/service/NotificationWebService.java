package dormease.dormeasedev.domain.notification.service;

import dormease.dormeasedev.domain.block.domain.Block;
import dormease.dormeasedev.domain.block.domain.repository.BlockRepository;
import dormease.dormeasedev.domain.block.dto.request.BlockReq;
import dormease.dormeasedev.domain.block.dto.request.UpdateBlockReq;
import dormease.dormeasedev.domain.block.dto.response.BlockRes;
import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.file.domain.repository.FileRepository;
import dormease.dormeasedev.domain.file.dto.response.FileRes;
import dormease.dormeasedev.domain.notification.domain.Notification;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notification.domain.repository.NotificationRepository;
import dormease.dormeasedev.domain.notification.dto.request.ModifyNotificationReq;
import dormease.dormeasedev.domain.notification.dto.request.WriteNotificataionReq;
import dormease.dormeasedev.domain.notification.dto.response.NotificationDetailWebRes;
import dormease.dormeasedev.domain.notification.dto.response.NotificationWebRes;
import dormease.dormeasedev.domain.s3.service.S3Uploader;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
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
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> writeNotification(CustomUserDetails customUserDetails, WriteNotificataionReq writeNotificataionReq, List<MultipartFile> multipartFiles) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Notification notification = Notification.builder()
                .school(school)
                .user(admin)
                .notificationType(writeNotificataionReq.getNotificationType())
                .title(writeNotificataionReq.getTitle())
                .pinned(writeNotificataionReq.getPinned())
                .build();
        notificationRepository.save(notification);

        List<BlockReq> blockReqList = writeNotificataionReq.getBlockReqList();
        createBlock(notification, blockReqList);
        uploadFiles(notification, multipartFiles);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("공지사항(FAQ) 등록이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 공지사항(FAQ) 상세 조회
    public ResponseEntity<?> findNotification(CustomUserDetails customUserDetails, Long notificationId) {

        // TODO : 제목, 작성자, 작성일, 수정일, 내용, 첨부파일, 최상단 고정 여부
        User admin = userService.validateUserById(customUserDetails.getId());
        Notification notification = validateById(notificationId);

        DefaultAssert.isTrue(admin.getSchool().equals(notification.getSchool()), "해당 관리자의 학교만 조회할 수 있습니다.");

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

        NotificationDetailWebRes notificationDetailWebRes = NotificationDetailWebRes.builder()
                .pinned(notification.getPinned())
                .title(notification.getTitle())
                .writer(notification.getUser().getName())
                .createdDate(notification.getCreatedDate().toLocalDate())
                .modifiedDate(notification.getModifiedDate().toLocalDate())
                .blockResList(blockResList)
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
    public ResponseEntity<?> modifyNotification(CustomUserDetails customUserDetails, ModifyNotificationReq modifyNotificationReq, List<MultipartFile> multipartFiles) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();
        Notification notification = validateById(modifyNotificationReq.getNotificationId());

        DefaultAssert.isTrue(admin.getSchool().equals(notification.getSchool()), "해당 관리자의 학교만 수정할 수 있습니다.");

        if (StringUtils.hasLength(modifyNotificationReq.getTitle()))
            notification.updateTitle(modifyNotificationReq.getTitle());

        if (modifyNotificationReq.getPinned() != null && notification.getPinned() != modifyNotificationReq.getPinned())
            notification.updatePinned();

        if (!modifyNotificationReq.getDeletedBlockIds().isEmpty())
            blockRepository.deleteAllById(modifyNotificationReq.getDeletedBlockIds());

        if (!modifyNotificationReq.getDeletedFileIds().isEmpty())
            deleteFiles(modifyNotificationReq.getDeletedFileIds(), school);

        if (!modifyNotificationReq.getCreateBlockReqList().isEmpty())
            createBlock(notification, modifyNotificationReq.getCreateBlockReqList());

        if (!modifyNotificationReq.getUpdateBlockReqList().isEmpty())
            updateBlock(notification, modifyNotificationReq.getUpdateBlockReqList());

        if (!multipartFiles.isEmpty())
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

        deleteFiles(notification, school);
        deleteBlock(notification, school);
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
    public void uploadFiles(Notification notification, List<MultipartFile> multipartFiles) {

        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl;
            if (multipartFile.isEmpty())
                fileUrl = null;
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

    // Description : 블럭 생성 함수
    public void createBlock(Notification notification, List<BlockReq> blockReqList) {

        for (BlockReq blockReq : blockReqList) {
            Block block = Block.builder()
                    .notification(notification)
                    .imageUrl(blockReq.getImageUrl())
                    .sequence(blockReq.getSequence())
                    .content(blockReq.getContent())
                    .build();
            blockRepository.save(block);
            block.addNotification(notification);
        }
    }

    // Description : 블럭 수정 함수
    private void updateBlock(Notification notification, List<UpdateBlockReq> updateBlockReqList) {

        for (UpdateBlockReq updateBlockReq : updateBlockReqList) {
            Long blockId = updateBlockReq.getBlockId();
            Optional<Block> findBlock = blockRepository.findById(blockId);
            DefaultAssert.isTrue(findBlock.isPresent(), "존재하지 않는 블럭입니다.");
            Block block = findBlock.get();

            block.updateBlock(updateBlockReq);
        }
    }

    public void deleteBlock(Notification notification, School school) {
        List<Block> blockList = blockRepository.findByNotification(notification);
        blockRepository.deleteAll(blockList);

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
