package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request.ModifyDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryApplicationSettingSimpleRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryRoomTypeRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingHistoryRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.service.DormitoryApplicationSettingService;
import dormease.dormeasedev.global.common.Message;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dormitory Appliation Setting API", description = "WEB - 입사 신청 설정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitoryApplicationSetting")
public class DormitoryApplicationSettingWebController {

    private final DormitoryApplicationSettingService dormitoryApplicationSettingService;

    @Operation(summary = "현재 입사 신청 설정 조회", description = "현재 입사 신청 설정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "현재 입사 신청 설정 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindDormitoryApplicationSettingRes.class))}),
            @ApiResponse(responseCode = "400", description = "현재 입사 신청 설정 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findNowDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationSettingService.findNowDormitoryApplicationSetting(userDetailsImpl);
    }

    @Operation(summary = "입사 신청 설정 생성", description = "입사 신청 설정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "입사 신청 설정 설정(생성) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 설정 설정(생성) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 CreateDormitoryApplicationSettingReq를 참고해주세요.", required = true) @Valid @RequestBody CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq
            ) {
        return dormitoryApplicationSettingService.createDormitoryApplicationSetting(userDetailsImpl, createDormitoryApplicationSettingReq);
    }

    @Operation(summary = "입사 신청 설정 조회", description = "입사 신청 설정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 설정 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindDormitoryApplicationSettingRes.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 설정 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryApplicationSettingId}")
    public ResponseEntity<?> findDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, // 관리자 id를 통해 학교를 알아내기 위함
            @Parameter(description = "입사 신청 설정 id를 입력해주세요.", required = true) @PathVariable Long dormitoryApplicationSettingId
    ) {
        return dormitoryApplicationSettingService.findDormitoryApplicationSetting(userDetailsImpl, dormitoryApplicationSettingId);
    }

    @Operation(summary = "입사 신청 설정 수정", description = "입사 신청 설정을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 설정 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 설정 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/{dormitoryApplicationSettingId}")
    public ResponseEntity<?> modifyDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 CreateDormitoryApplicationSettingReq를 참고해주세요.", required = true) @RequestBody ModifyDormitoryApplicationSettingReq modifyDormitoryApplicationSettingReq,
            @Parameter(description = "입사 신청 설정 id를 입력해주세요.", required = true) @PathVariable Long dormitoryApplicationSettingId

    ) {
        dormitoryApplicationSettingService.modifyDormitoryApplicationSetting(userDetailsImpl, modifyDormitoryApplicationSettingReq, dormitoryApplicationSettingId);
        return ResponseEntity.noContent().build();
    }

    // Description : 이전 입사 신청 설정 목록 3개 조회
    @Operation(summary = "이전 입사 신청 설정 목록 3개 조회", description = "이전 입사 신청 설정 목록을 최신 순 3개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이전 입사 신청 설정 목록 3개 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FindDormitoryApplicationSettingHistoryRes.class)))}),
            @ApiResponse(responseCode = "400", description = "이전 입사 신청 설정 목록 3개 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/topThree")
    public ResponseEntity<?> findDormitoryApplicationSettingHistory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationSettingService.findDormitoryApplicationSettingHistory(userDetailsImpl);
    }

    // Description : 입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회
    @Operation(summary = "입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회", description = "입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryRoomTypeRes.class)))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/dormitories")
    public ResponseEntity<?> findDormitories(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationSettingService.findDormitories(userDetailsImpl);
    }

    // Description : 이전 작성 내용 목록 조회
    @Operation(summary = "이전 작성 내용 목록 조회", description = "이전 작성 내용 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이전 작성 내용 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationSettingSimpleRes.class)))}),
            @ApiResponse(responseCode = "400", description = "이전 작성 내용 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/history")
    public ResponseEntity<?> findDormitoryApplicationSettings(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationSettingService.findDormitoryApplicationSettings(userDetailsImpl);
    }
}
