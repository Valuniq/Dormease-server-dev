package dormease.dormeasedev.domain.refund_requestment.controller;

import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryManagementListRes;
import dormease.dormeasedev.domain.refund_requestment.dto.response.RefundRequestmentRes;
import dormease.dormeasedev.domain.refund_requestment.service.RefundRequestmentService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RefundRequestment API", description = "WEB - 환불 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/refundRequestment")
public class RefundRequestmentController {

    private final RefundRequestmentService refundRequestmentService;

    // Description : 환불 신청 사생 목록 조회
    @Operation(summary = "환불 신청 사생 목록 조회", description = "환불 신청 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불 신청 사생 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RefundRequestmentRes.class)))}),
            @ApiResponse(responseCode = "400", description = "환불 신청 사생 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/residents")
    public ResponseEntity<?> findResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return refundRequestmentService.findResidents(customUserDetails);
    }

    // Description : 환불 신청한 사생 처리(삭제)
    @Operation(summary = "환불 완료 사생 처리 (삭제)", description = "환불 완료 사생을 처리(삭제)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불 완료 사생 처리(삭제) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "환불 완료 사생 처리(삭제) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> deleteResident(
            @Parameter(description = "처리(삭제)할 사생의 ID를 입력해주세요.", required = true) @PathVariable(value = "residentId") Long residentId
    ) {
        return refundRequestmentService.deleteResident(residentId);
    }
}
