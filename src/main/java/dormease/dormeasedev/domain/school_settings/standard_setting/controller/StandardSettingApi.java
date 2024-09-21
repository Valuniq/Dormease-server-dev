package dormease.dormeasedev.domain.school_settings.standard_setting.controller;

import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminNameReq;
import dormease.dormeasedev.domain.users.admin.dto.response.AdminUserInfoRes;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[WEB] Standard Setting API", description = "WEB에서 사용될 기준 설정 관련 API입니다.")
public interface StandardSettingApi {

    @Operation(summary = "기준 설정 생성 API", description = "기준 설정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "기준 설정 생성 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "기준 설정 생성 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PostMapping
    ResponseEntity<Void> createStandardSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 ModifyAdminNameReq을 참고해주세요.", required = true) @Valid @RequestBody CreateStandardSettingReq createStandardSettingReq
    );
}
