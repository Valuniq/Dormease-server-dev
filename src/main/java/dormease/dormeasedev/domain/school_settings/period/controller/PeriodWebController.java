package dormease.dormeasedev.domain.school_settings.period.controller;

import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import dormease.dormeasedev.domain.school_settings.period.dto.request.PeriodReq;
import dormease.dormeasedev.domain.school_settings.period.dto.response.PeriodRes;
import dormease.dormeasedev.domain.school_settings.period.service.PeriodWebService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
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

@Tag(name = "Period API", description = "WEB - 기간 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/period")
public class PeriodWebController {

    private final PeriodWebService periodWebService;

    // Description : 신청 기간 등록 / 수정
    @Operation(summary = "신청 기간 등록 / 수정", description = "타입에 맞는 신청 기간을 등록 / 수정합니다. 같은 타입의 **기존 신청 기간이 존재하는 경우 날짜만 변경합니다**.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신청 기간 등록 / 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "신청 기간 등록 / 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> registerPeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 PeriodReq를 참고해주세요.", required = true) @RequestBody PeriodReq periodReq
    ) {
        return periodWebService.registerPeriod(customUserDetails, periodReq);
    }

    // Description : 신청 기간 조회
    @Operation(summary = "신청 기간 조회", description = "타입에 맞는 신청 기간을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 기간 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PeriodRes.class))}),
            @ApiResponse(responseCode = "400", description = "신청 기간 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("{periodType}")
    public ResponseEntity<?> findPeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "조회할 기간의 타입을 입력해주세요. LEAVE(퇴사) / REFUND(환불) / ROOMMATE(룸메이트) 中 1.", required = true) @PathVariable(value = "periodType") PeriodType periodType
            ) {
        return periodWebService.findPeriod(customUserDetails, periodType);
    }
}
