package dormease.dormeasedev.domain.period.controller;

import dormease.dormeasedev.domain.period.dto.request.PeriodReq;
import dormease.dormeasedev.domain.period.service.PeriodWebService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Period API", description = "WEB - 기간 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/period")
public class PeriodWebController {

    private final PeriodWebService periodWebService;

    // Description : 신청 기간 등록
    @Operation(summary = "신청 기간 등록", description = "신청 기간 등록. 같은 타입의 기존 신청 기간이 존재하는 경우 **삭제 후 생성합니다**.") // TODO : 아예 타입 받아서 공용으로?
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신청 기간 검증 등록", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "신청 기간 검증 등록", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> registerPeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @RequestBody PeriodReq periodReq
    ) {
        return periodWebService.registerPeriod(customUserDetails, periodReq);
    }
}
