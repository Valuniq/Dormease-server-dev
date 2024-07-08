package dormease.dormeasedev.domain.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_application.dto.response.DormitoryApplicationUserRes;
import dormease.dormeasedev.domain.dormitory_application.service.DormitoryApplicationWebService;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ExitRequestmentResidentRes;
import dormease.dormeasedev.domain.period.dto.response.PeriodDateRes;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
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

@Tag(name = "Dormitory Appliation API", description = "WEB - 입사 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitoryApplication")
public class DormitoryApplciationWebController {

    private final DormitoryApplicationWebService dormitoryApplicationWebService;

    @Operation(summary = "현재 입사 신청자 명단 조회", description = "현재 입사 신청자 명단을 조회합니다. (페이징 x, 한 번에 전체 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "현재 입사 신청자 명단 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationUserRes.class)))}),
            @ApiResponse(responseCode = "400", description = "현재 입사 신청자 명단 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findNowDormitoryApplicationList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return dormitoryApplicationWebService.findNowDormitoryApplicationList(customUserDetails);
    }

    @Operation(summary = "입사 신청자 명단 조회", description = "입사 신청자 명단을 조회합니다. 입사 신청 설정 id로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "입사 신청자 명단 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationUserRes.class)))}),
            @ApiResponse(responseCode = "200", description = "입사 신청자 명단 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청자 명단 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryApplicationSettingId}")
    public ResponseEntity<?> findDormitoryApplicationList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "입사 신청자 명단을 조회할 입사 신청 설정 id를 입력해주세요.", required = true) @PathVariable(value = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return dormitoryApplicationWebService.findDormitoryApplicationList(customUserDetails, dormitoryApplicationSettingId, page - 1);
    }

}
