package dormease.dormeasedev.domain.user.controller;

import dormease.dormeasedev.domain.user.dto.request.*;
import dormease.dormeasedev.domain.user.dto.response.FindLoginIdRes;
import dormease.dormeasedev.domain.user.dto.response.FindMyInfoRes;
import dormease.dormeasedev.domain.user.service.UserService;
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

@Tag(name = "User API", description = "User 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 아이디 찾기", description = "로그인 아이디를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindLoginIdRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/loginId")
    public ResponseEntity<?> findLoginId(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 FindLoginIdRequest를 참고해주세요.", required = true) @RequestBody FindLoginIdReq findLoginIdReq
    ) {
        return userService.findLoginId(customUserDetails, findLoginIdReq);
    }

    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재설정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "재설정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/password")
    public ResponseEntity<?> modifyPassword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 FindPasswordReq 참고해주세요.", required = true) @RequestBody FindPasswordReq findPasswordReq
    ) {
        return userService.modifyPassword(customUserDetails, findPasswordReq);
    }

    @Operation(summary = "내 정보 조회 ", description = "내 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindMyInfoRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findMyInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return userService.findMyInfo(customUserDetails);
    }

    @Operation(summary = "내 정보 수정 - 학번(수험번호) 수정 ", description = "학번(수험번호)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/studentNumber")
    public ResponseEntity<?> modifyStudentNumber (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 ModifyStudentNumberReq 참고해주세요.", required = true) @Valid @RequestBody ModifyStudentNumberReq modifyStudentNumberReq
    ) {
        return userService.modifyStudentNumber(customUserDetails, modifyStudentNumberReq);
    }

    @Operation(summary = "내 정보 수정 - 비밀번호 재설정 ", description = "비밀번호를 재설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/mypage/password")
    public ResponseEntity<?> resetPasswordInMyPage (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 ResetPasswordReq 참고해주세요.", required = true) @Valid @RequestBody ResetPasswordReq resetPasswordReq
    ) {
        return userService.resetPasswordInMyPage(customUserDetails, resetPasswordReq);
    }

    @Operation(summary = "내 정보 수정 - 휴대전화 번호 재설정 ", description = "휴대전화 번호를 재설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/phoneNumber")
    public ResponseEntity<?> modifyPhoneNumber (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 ModifyPhoneNumberReq 참고해주세요.", required = true) @Valid @RequestBody ModifyPhoneNumberReq modifyPhoneNumberReq
    ) {
        return userService.modifyPhoneNumber(customUserDetails, modifyPhoneNumberReq);
    }
}
