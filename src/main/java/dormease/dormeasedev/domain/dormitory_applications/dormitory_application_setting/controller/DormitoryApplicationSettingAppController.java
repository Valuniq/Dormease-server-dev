package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.service.DormitoryApplicationSettingAppService;
import dormease.dormeasedev.domain.school_settings.period.dto.response.PeriodDateRes;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dormitory Appliation StandardSetting API", description = "APP - 입사 신청 설정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/dormitoryApplicationSetting")
public class DormitoryApplicationSettingAppController {

    private final DormitoryApplicationSettingAppService dormitoryApplicationSettingAppService;

    @Operation(summary = "입사 신청 기간 조회", description = "입사 신청 기간인지 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PeriodDateRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> validateDormitoryApplicationPeriod(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationSettingAppService.validateDormitoryApplicationPeriod(userDetailsImpl);
    }


}
