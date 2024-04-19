package dormease.dormeasedev.domain.dormitory_application_setting.controller;

import dormease.dormeasedev.domain.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingHistoryRes;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory_application_setting.service.DormitoryApplicationSettingService;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dormitory Appliation Setting API", description = "입사 신청 설정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitory-application-setting")
public class DormitoryApplicationSettingController {

    private final DormitoryApplicationSettingService dormitoryApplicationSettingService;

    @Operation(summary = "입사 신청 설정 생성", description = "입사 신청 설정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "설정(생성) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "설정(생성) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails, // 관리자 id를 통해 학교를 알아내기 위함
            @Parameter(description = "Schemas의 CreateDormitoryApplicationSettingReq를 참고해주세요.", required = true) @Valid @RequestBody CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq
            ) {
        return dormitoryApplicationSettingService.createDormitoryApplicationSetting(customUserDetails, createDormitoryApplicationSettingReq);
    }

    @Operation(summary = "입사 신청 설정 조회", description = "입사 신청 설정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindDormitoryApplicationSettingRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryApplicationSettingId}")
    public ResponseEntity<?> findDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails, // 관리자 id를 통해 학교를 알아내기 위함
            @Parameter(description = "입사 신청 설정 id를 입력해주세요.", required = true) @PathVariable Long dormitoryApplicationSettingId
    ) {
        return dormitoryApplicationSettingService.findDormitoryApplicationSetting(customUserDetails, dormitoryApplicationSettingId);
    }

//    @Operation(summary = "입사 신청 설정 수정", description = "입사 신청 설정을 수정합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = .class))}),
//            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @PatchMapping("/{dormitoryApplicationSettingId}")
//    public ResponseEntity<?> modifyDormitoryApplicationSetting(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails, // 관리자 id를 통해 학교를 알아내기 위함
//            @Parameter(description = "Schemas의 CreateDormitoryApplicationSettingReq를 참고해주세요.", required = true) @RequestBody CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq,
//            @Parameter(description = "입사 신청 설정 id를 입력해주세요.", required = true) @PathVariable Long dormitoryApplicationSettingId
//
//    ) {
//        return dormitoryApplicationSettingService.modifyDormitoryApplicationSetting(customUserDetails, createDormitoryApplicationSettingReq, dormitoryApplicationSettingId);
//    }

    // Description : 이전 작성 내용 목록 조회
    @Operation(summary = "이전 작성 내용 목록 조회", description = "이전 작성 내용 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FindDormitoryApplicationSettingHistoryRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/history")
    public ResponseEntity<?> findDormitoryApplicationSettingHistory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails, // 관리자 id를 통해 학교를 알아내기 위함
            @Parameter(description = "이전 작성 내용 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page
    ) {
        return dormitoryApplicationSettingService.findDormitoryApplicationSettingHistory(customUserDetails, page);
    }


}
