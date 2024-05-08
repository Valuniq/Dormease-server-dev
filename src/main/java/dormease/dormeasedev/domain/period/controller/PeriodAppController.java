package dormease.dormeasedev.domain.period.controller;

import dormease.dormeasedev.domain.period.service.PeriodAppService;
import dormease.dormeasedev.domain.period.domain.PeriodType;
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

@Tag(name = "Period API", description = "APP - 기간 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/period")
public class PeriodAppController {

    private final PeriodAppService periodAppService;

    // Description : 신청 기간 검증
    @Operation(summary = "신청 기간 검증", description = "신청 기간 검증") // TODO : 아예 타입 받아서 공용으로?
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 기간 검증 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "신청 기간 검증 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{periodType}")
    public ResponseEntity<?> validatePeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "기간 타입을 입력해주세요. JOIN(입사) / LEAVE(퇴사) / REFUND(환불) / ROOMMATE(룸메이트) / DEPOSIT(입금) 中 1", required = true) @PathVariable(value = "periodType") PeriodType periodType
    ) {
        return periodAppService.validatePeriod(customUserDetails,periodType);
    }


}
