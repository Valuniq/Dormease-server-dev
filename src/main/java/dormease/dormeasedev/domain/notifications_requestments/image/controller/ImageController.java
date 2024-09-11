package dormease.dormeasedev.domain.notifications_requestments.image.controller;

import dormease.dormeasedev.domain.notifications_requestments.image.dto.request.DeleteImageFromS3Req;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.response.ImageUrlRes;
import dormease.dormeasedev.domain.notifications_requestments.image.service.ImageService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Images API", description = "WEB - 공지사항(FAQ) image 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/images")
public class ImageController {

    private final ImageService imageService;

    // Description : s3에 이미지 올린 후 이미지 url 응답
    @Operation(summary = "공지사항(FAQ) 이미지 url 반환" , description = "공지사항(FAQ) 이미지 url을 반환합니다. (텍스트 에디터 이미지 첨부 버튼 클릭 시 사용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 이미지 url을 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ImageUrlRes.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 이미지 url을 반환 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> uploadImageToS3(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "업로드할 이미지 파일 (Multipart form-data 형식)") @RequestPart(value = "image") MultipartFile multipartFile
    ) {
        return imageService.uploadImageToS3(customUserDetails, multipartFile);
    }

    // Description : 텍스트 에디터 작성 중 이탈 시 s3에서 삭제
    @Operation(summary = "공지사항(FAQ) 이미지 url 삭제" , description = "공지사항(FAQ) 이미지 url을 삭제합니다. (텍스트 에디터 이미지 첨부 후 이탈 시 사용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 이미지 url을 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 이미지 url을 반환 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteImageFromS3(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "삭제할 이미지 url 목록을 입력해주세요.") @RequestBody DeleteImageFromS3Req deleteImageFromS3Req
    ) {
        return imageService.deleteImageFromS3(customUserDetails, deleteImageFromS3Req);
    }
}
