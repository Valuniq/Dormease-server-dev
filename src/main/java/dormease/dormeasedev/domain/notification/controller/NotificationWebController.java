package dormease.dormeasedev.domain.notification.controller;

import dormease.dormeasedev.domain.block.dto.request.BlockReq;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notification.dto.request.ModifyNotificationReq;
import dormease.dormeasedev.domain.notification.dto.request.WriteNotificataionReq;
import dormease.dormeasedev.domain.notification.dto.response.NotificationDetailRes;
import dormease.dormeasedev.domain.notification.dto.response.NotificationRes;
import dormease.dormeasedev.domain.notification.service.NotificationWebService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Common API", description = "APP - 플로우를 위한 공용 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/notifications")
public class NotificationWebController {

    private final NotificationWebService notificationWebService;

    // Description : 공지사항(FAQ) 목록 조회
    @Operation(summary = "공지사항(FAQ) 목록 조회" , description = "공지사항(FAQ) 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "공지사항(FAQ) 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificationRes.class)))}),
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{notificationType}")
    public ResponseEntity<?> findNotifications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Notification Type을 입력해주세요. ANNOUNCEMENT(공지사항) / FAQ 中 1입니다.", required = true) @PathVariable(value = "notificationType") NotificationType notificationType,
            @Parameter(description = " 퇴사 신청 사생 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return notificationWebService.findNotifications(customUserDetails, notificationType, page - 1);
    }

    // Description : 공지사항(FAQ) 작성
    @Operation(summary = "공지사항(FAQ) 작성" , description = "공지사항(FAQ)를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공지사항(FAQ) 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> writeNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 WriteNotificataionReq을 참고해주세요.", required = true) @RequestPart("writeNotificataionReq") WriteNotificataionReq writeNotificataionReq,
            @Parameter(description = "Schemas의 BlockReq을 참고해주세요.", required = false)@RequestPart("files") List<MultipartFile> multipartFiles
    ) {
        return notificationWebService.writeNotification(customUserDetails, writeNotificataionReq, multipartFiles);
    }

    // Description : 공지사항(FAQ) 상세 조회
    @Operation(summary = "공지사항(FAQ) 상세 조회" , description = "공지사항(FAQ)을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/notification/{notificationId}")
    public ResponseEntity<?> findNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Notification ID을 입력해주세요.", required = true) @PathVariable(value = "notificationId") Long notificationId
    ) {
        return notificationWebService.findNotification(customUserDetails, notificationId);
    }

     // Description : 공지사항(FAQ) 수정
    @Operation(summary = "공지사항(FAQ) 수정" , description = "공지사항(FAQ)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping
    public ResponseEntity<?> modifyNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 ModifyNotificationReq을 참고해주세요.", required = false) @RequestPart("modifyNotificationReq") ModifyNotificationReq modifyNotificationReq,
            @Parameter(description = "Schemas의 BlockReq을 참고해주세요.", required = false)@RequestPart("files") List<MultipartFile> multipartFiles
    ) {
        return notificationWebService.modifyNotification(customUserDetails, modifyNotificationReq, multipartFiles);
    }

    // Description : 공지사항(FAQ) 삭제
    @Operation(summary = "공지사항(FAQ) 삭제", description = "공지사항(FAQ)을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Notification ID을 입력해주세요.", required = true) @PathVariable(value = "notificationId") Long notificationId
    ) {
        return notificationWebService.deleteNotification(customUserDetails, notificationId);
    }

}
