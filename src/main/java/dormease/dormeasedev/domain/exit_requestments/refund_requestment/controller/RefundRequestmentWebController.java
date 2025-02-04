package dormease.dormeasedev.domain.exit_requestments.refund_requestment.controller;

import dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.response.MainRefundRequestmentRes;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.response.RefundRequestmentRes;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.service.RefundRequestmentWebService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RefundRequestment API", description = "WEB - 환불 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/refundRequestment")
public class RefundRequestmentWebController {

    private final RefundRequestmentWebService refundRequestmentWebService;

    // Description : 환불 신청 사생 목록 조회
    @Operation(summary = "환불 신청 사생 목록 조회", description = "환불 신청 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "환불 신청 사생 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RefundRequestmentRes.class)))}),
            @ApiResponse(responseCode = "200", description = "환불 신청 사생 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "환불 신청 사생 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "환불 신청 사생 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return refundRequestmentWebService.findResidents(userDetailsImpl, page - 1);
    }

    // Description : 환불 신청한 사생 처리(삭제)
    @Operation(summary = "환불 요청 완료 처리 (삭제)", description = "환불 요청 완료 처리(삭제)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불 완료 사생 처리(삭제) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "환불 완료 사생 처리(삭제) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{refundRequestmentId}")
    public ResponseEntity<?> deleteRefundRequestment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "처리(삭제)할 사생의 ID를 입력해주세요.", required = true) @PathVariable(value = "refundRequestmentId") Long refundRequestmentId
    ) {
        return refundRequestmentWebService.deleteRefundRequestment(userDetailsImpl, refundRequestmentId);
    }

    // Description : 메인 화면 - 환불 신청 사생 목록 조회
    @Operation(summary = "메인 화면 - 환불 신청 사생 목록 조회", description = "메인 화면 - 환불 신청 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "메인 화면 - 환불 신청 사생 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MainRefundRequestmentRes.class)))}),
            @ApiResponse(responseCode = "400", description = "메인 화면 - 환불 신청 사생 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/main")
    public ResponseEntity<?> findMainRefund(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return refundRequestmentWebService.findMainRefund(userDetailsImpl);
    }
}
