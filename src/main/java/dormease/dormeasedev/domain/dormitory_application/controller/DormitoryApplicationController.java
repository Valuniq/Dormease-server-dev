package dormease.dormeasedev.domain.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.domain.dormitory_application.dto.response.DormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_application.service.DormitoryApplicationService;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dormitory Appliation API", description = "입사 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/dormitoryApplication")
public class DormitoryApplicationController {

    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 입사 신청
    @Operation(summary = "입사 신청", description = "입사 신청을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "입사 신청(생성) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청(생성) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> dormitoryApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 DormitoryApplicationReq를 참고해주세요.", required = true) @Valid @RequestBody DormitoryApplicationReq dormitoryApplicationReq
    ) {
        return dormitoryApplicationService.dormitoryApplication(customUserDetails, dormitoryApplicationReq);
    }

    // Description : 이동 합격 수락 / or 수락 + 거절


    // Description : 입사 신청 내역 조회 (여기서 내역은 history보단 이번에 신청한 내역을 말하는 것)
    @Operation(summary = "입사 신청 내역 조회", description = "입사 신청 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 내역 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryApplicationRes.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 내역 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findMyDormitoryApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return dormitoryApplicationService.findMyDormitoryApplication(customUserDetails);
    }

}
