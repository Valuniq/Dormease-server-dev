package dormease.dormeasedev.domain.notifications_requestments.notification.controller;

import dormease.dormeasedev.domain.notifications_requestments.notification.domain.NotificationType;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationAppRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationDetailAppRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.dto.response.NotificationMainRes;
import dormease.dormeasedev.domain.notifications_requestments.notification.service.NotificationAppService;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
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

@Tag(name = "Notifications API", description = "APP - 공지사항(FAQ) 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/notifications")
public class NotificationAppController {

    private final NotificationAppService notificationAppService;

    // Description : 공지사항(FAQ) 목록 조회
    @Operation(summary = "공지사항(FAQ) 목록 조회" , description = "공지사항(FAQ) 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "공지사항(FAQ) 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificationAppRes.class)))}),
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{notificationType}")
    public ResponseEntity<?> findNotifications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Notification Type을 입력해주세요. ANNOUNCEMENT(공지사항) / FAQ 中 1입니다.", required = true) @PathVariable(value = "notificationType") NotificationType notificationType,
            @Parameter(description = " 퇴사 신청 사생 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return notificationAppService.findNotifications(customUserDetails, notificationType, page - 1);
    }

    // Description : 공지사항(FAQ) 상세 조회
    @Operation(summary = "공지사항(FAQ) 상세 조회" , description = "공지사항(FAQ)을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항(FAQ) 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationDetailAppRes.class))}),
            @ApiResponse(responseCode = "400", description = "공지사항(FAQ) 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/notification/{notificationId}")
    public ResponseEntity<?> findNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Notification ID을 입력해주세요.", required = true) @PathVariable(value = "notificationId") Long notificationId
    ) {
        return notificationAppService.findNotification(customUserDetails, notificationId);
    }

    // Description : 메인 페이지 공지사항 조회 - 상단핀 고정된 제일 최근 공지사항
    @Operation(summary = "메인 페이지 공지사항 조회" , description = "메인 페이지 공지사항을 조회합니다. 상단핀 고정된 제일 최근 공지사항입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인 페이지 공지사항 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationMainRes.class))}),
            @ApiResponse(responseCode = "400", description = "메인 페이지 공지사항 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findMainNotification(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return notificationAppService.findMainNotification(customUserDetails);
    }
}
