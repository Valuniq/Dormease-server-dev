package dormease.dormeasedev.domain.users.admin.controller;

import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminNameReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminPasswordReq;
import dormease.dormeasedev.domain.users.admin.dto.request.CheckSecurityCodeReq;
import dormease.dormeasedev.domain.users.admin.dto.response.AdminUserInfoRes;
import dormease.dormeasedev.domain.users.admin.dto.response.CheckSecurityCodeRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[WEB] Admin Account API", description = "WEB에서 관리자 계정 관리 프로세스 API입니다.")
public interface AdminApi {

    @Operation(summary = "관리자 소속 학교, 아이디, 이름 조회 API", description = "관리자 소속 학교, 아이디, 이름을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "관리자 소속 학교, 아이디, 이름 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AdminUserInfoRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "관리자 소속 학교, 아이디, 이름 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findAdminUserInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "관리자 이름 변경 API", description = "관리자 이름을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "관리자 이름 변경 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "관리자 이름 변경 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PutMapping("/name")
    ResponseEntity<Void> modifyAdminUserName(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 ModifyAdminNameReq을 참고해주세요.", required = true) @Valid @RequestBody ModifyAdminNameReq modifyAdminNameReq
    );

    @Operation(summary = "관리자 비밀번호 변경 API", description = "관리자 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "관리자 이름 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "관리자 이름 수정 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PutMapping("/password")
    ResponseEntity<Void> modifyAdminUserPassword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 ModifyAdminPasswordReq을 참고해주세요.", required = true) @Valid @RequestBody ModifyAdminPasswordReq modifyAdminPasswordReq
    );

    @Operation(summary = "보안 코드 검증 API", description = "관리자의 보안 코드를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "관리자 보안 코드 검증 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckSecurityCodeRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "관리자 보안 코드 검증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/securityCode")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> checkSecurityCode(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "보안 코드를 입력해주세요.", required = true) @RequestParam(value = "securityCode") String securityCode
    );
}
