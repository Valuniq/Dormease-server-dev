package dormease.dormeasedev.domain.notifications_requestments.image.service;

import dormease.dormeasedev.domain.notifications_requestments.image.dto.request.DeleteImageFromS3Req;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.response.ImageUrlRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.infrastructure.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {

    private final S3Uploader s3Uploader;
    private final UserService userService;

    public ResponseEntity<?> uploadImageToS3(CustomUserDetails customUserDetails, MultipartFile multipartFile) {

        User admin = userService.validateUserById(customUserDetails.getId());
        String imageUrl = uploadImage(multipartFile);
        ImageUrlRes imageUrlRes = ImageUrlRes.builder()
                .imageUrl(imageUrl)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageUrlRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> deleteImageFromS3(CustomUserDetails customUserDetails, DeleteImageFromS3Req deleteImageFromS3Req) {

        User admin = userService.validateUserById(customUserDetails.getId());
        deleteImages(deleteImageFromS3Req);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("이미지가 S3에서 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 단일 이미지 업로드 함수 (S3에 업로드, image 테이블에 저장 x) (텍스트 에디터 이미지 첨부 버튼)
    public String uploadImage(MultipartFile multipartFile) {

        if (multipartFile.isEmpty())
            return null;
        // 업로드
        String imageUrl = s3Uploader.uploadImage(multipartFile);
        // 반환
        return imageUrl;
    }

    // Description : 이미지 이름으로 S3에서 삭제

    // Description : s3에서 이미지 삭제 + image에서 삭제 (notification)
    public void deleteImages(DeleteImageFromS3Req deleteImageFromS3Req) {

        List<String> imageUrlList = deleteImageFromS3Req.getImageUrlList();
        if (!imageUrlList.isEmpty()) {
            for (String imageUrl : imageUrlList) {
                // s3에서 삭제
                String imageName = imageUrl.split("amazonaws.com/")[1];
                s3Uploader.deleteFile(imageName);
            }
        }
    }

}
