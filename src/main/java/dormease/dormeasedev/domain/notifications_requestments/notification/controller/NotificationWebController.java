package dormease.dormeasedev.domain.notifications_requestments.notification.controller;

import dormease.dormeasedev.domain.notifications_requestments.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.request.ModifyNotificationReq;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.request.WriteNotificationReq;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.MainNotificationRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationDetailWebRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationWebRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.service.NotificationWebService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(name = "Notifications API", description = "WEB - 공지사항(FAQ) 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/notifications")
public class NotificationWebController {

    private final NotificationWebService notificationWebService;

    // Description : 공지사항(FAQ) 목록 조회
    @Operation(summary = "공지사항(FAQ) 목록 조회" , description = "공지사항(FAQ) 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "공지사항(FAQ) 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificationWebRes.class)))}),
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{notificationType}")
    public ResponseEntity<?> findNotifications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Notification Type을 입력해주세요. ANNOUNCEMENT(공지사항) / FAQ 中 1입니다.", required = true) @PathVariable(value = "notificationType") NotificationType notificationType,
            @Parameter(description = " 퇴사 신청 사생 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return notificationWebService.findNotifications(userDetailsImpl, notificationType, page - 1);
    }

    // Description : 공지사항(FAQ) 작성
    @Operation(summary = "공지사항(FAQ) 작성" , description = "공지사항(FAQ)를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공지사항(FAQ) 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> writeNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 WriteNotificataionReq을 참고해주세요.", required = true) @RequestPart("writeNotificationReq") WriteNotificationReq writeNotificationReq,
            @Parameter(description = "업로드할 이미지 파일 목록 (Multipart form-data 형식).")@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) throws IOException {
        Long notificationId = notificationWebService.writeNotification(userDetailsImpl, writeNotificationReq, multipartFiles);
        return ResponseEntity.created(URI.create("/api/v1/web/notifications/notification/" + notificationId)).build();
    }

    // Description : 공지사항(FAQ) 상세 조회
    @Operation(summary = "공지사항(FAQ) 상세 조회" , description = "공지사항(FAQ)을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationDetailWebRes.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/notification/{notificationId}")
    public ResponseEntity<?> findNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Notification ID을 입력해주세요.", required = true) @PathVariable(value = "notificationId") Long notificationId
    ) {
        return notificationWebService.findNotification(userDetailsImpl, notificationId);
    }

     // Description : 공지사항(FAQ) 수정
    @Operation(summary = "공지사항(FAQ) 수정" , description = "공지사항(FAQ)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping
    public ResponseEntity<?> modifyNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 ModifyNotificationReq을 참고해주세요.") @RequestPart("modifyNotificationReq") ModifyNotificationReq modifyNotificationReq,
            @Parameter(description = "업로드할 이미지 파일 목록 (Multipart form-data 형식).") @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) throws IOException {
        return notificationWebService.modifyNotification(userDetailsImpl, modifyNotificationReq, multipartFiles);
    }

    // Description : 공지사항(FAQ) 삭제
    @Operation(summary = "공지사항(FAQ) 삭제", description = "공지사항(FAQ)을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Notification ID을 입력해주세요.", required = true) @PathVariable(value = "notificationId") Long notificationId
    ) {
        return notificationWebService.deleteNotification(userDetailsImpl, notificationId);
    }

    // Description : 메인 화면 - 공지사항(FAQ) 목록 조회
    @Operation(summary = "메인 화면 - 공지사항 목록 조회" , description = "메인 화면 - 공지사항 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "메인 화면 - 공지사항 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MainNotificationRes.class)))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/main")
    public ResponseEntity<?> findNotifications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return notificationWebService.findMainNotifications(userDetailsImpl);
    }
}
