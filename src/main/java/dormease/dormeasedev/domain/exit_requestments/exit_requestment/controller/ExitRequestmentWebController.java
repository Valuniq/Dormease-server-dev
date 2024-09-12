package dormease.dormeasedev.domain.exit_requestments.exit_requestment.controller;

import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.request.DeleteExitRequestmentReq;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.request.ModifyDepositReq;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response.ExitRequestmentRes;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response.ExitRequestmentResidentRes;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.service.ExitRequestmentWebService;
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

@Tag(name = "ExitRequestment API", description = "WEB - 퇴사 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/exitRequestment")
public class ExitRequestmentWebController {

    private final ExitRequestmentWebService exitRequestmentWebService;

    // Description : 퇴사 신청 사생 목록 조회
    @Operation(summary = "퇴사 신청 사생 목록 조회", description = "퇴사 신청 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "퇴사 신청 사생 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExitRequestmentResidentRes.class)))}),
            @ApiResponse(responseCode = "200", description = "퇴사 신청 사생 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "퇴사 신청 사생 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/residents")
    public ResponseEntity<?> findResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = " 퇴사 신청 사생 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return exitRequestmentWebService.findResidents(customUserDetails, page - 1);
    }

    // Description : 퇴사 신청서 조회
    @Operation(summary = "퇴사 신청 상세 조회", description = "퇴사 신청을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퇴사 신청 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExitRequestmentRes.class))}),
            @ApiResponse(responseCode = "400", description = "퇴사 신청 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{exitRequestmentId}")
    public ResponseEntity<?> findExitRequestment(
            @Parameter(description = "exitRequestmentId(퇴사 신청 id)를 입력해주세요.", required = true) @PathVariable(name = "exitRequestmentId") Long exitRequestmentId
    ) {
        return exitRequestmentWebService.findExitRequestment(exitRequestmentId);
    }

    // Description : 보증금 환급 상태 변경
    @Operation(summary = "보증금 환급 상태 변경", description = "보증금 환급 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보증금 환급 상태 변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "보증금 환급 상태 변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/securityDeposit")
    public ResponseEntity<?> modifySecurityDeposit(
            @Parameter(description = "Schemas의 ModifyDepositReq를 참고해주세요.", required = true) @RequestBody ModifyDepositReq modifyDepositReq
    ) {
        return exitRequestmentWebService.modifySecurityDeposit(modifyDepositReq);
    }

    // Description : 퇴사 신청서 삭제
    @Operation(summary = "퇴사 신청서 삭제", description = "퇴사 신청서를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퇴사 신청서 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "퇴사 신청서 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteExitRequestment(
            @Parameter(description = "Schemas의 DeleteExitRequestmentReq를 참고해주세요.", required = true) @RequestBody DeleteExitRequestmentReq deleteExitRequestmentReq
    ) {
        return exitRequestmentWebService.deleteExitRequestment(deleteExitRequestmentReq);
    }

}
