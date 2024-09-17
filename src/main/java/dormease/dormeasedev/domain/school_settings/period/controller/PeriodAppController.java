package dormease.dormeasedev.domain.school_settings.period.controller;

import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import dormease.dormeasedev.domain.school_settings.period.service.PeriodAppService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Period API", description = "APP - 기간 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/period")
public class PeriodAppController {

    private final PeriodAppService periodAppService;

    // Description : 신청 기간 검증
    @Operation(summary = "신청 기간 검증", description = "신청 기간 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 기간 검증 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "신청 기간 검증 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{periodType}")
    public ResponseEntity<?> validatePeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "기간 타입을 입력해주세요. LEAVE(퇴사) / REFUND(환불) / ROOMMATE(룸메이트) 中 1", required = true) @PathVariable(value = "periodType") PeriodType periodType
    ) {
        return periodAppService.validatePeriod(userDetailsImpl,periodType);
    }



}
