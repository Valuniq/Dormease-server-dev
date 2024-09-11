package dormease.dormeasedev.domain.exit_requestments.refund_requestment.controller;

import dormease.dormeasedev.domain.exit_requestments.refund_requestment.service.RefundRequestmentAppService;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.request.RefundRequestmentReq;
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

@Tag(name = "RefundRequestment API", description = "APP - 환불 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/refundRequestment")
public class RefundRequestmentAppController {

    private final RefundRequestmentAppService refundRequestmentAppService;

    // Description : 환불 신청
    @Operation(summary = "환불 신청", description = "환불 신청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불 신청 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "환불 신청 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> requestRefund(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 RefundRequestmentReq을 참고해주세요.", required = true) @RequestBody RefundRequestmentReq refundRequestmentReq

    ) {
        return refundRequestmentAppService.requestRefund(customUserDetails, refundRequestmentReq);
    }
}
