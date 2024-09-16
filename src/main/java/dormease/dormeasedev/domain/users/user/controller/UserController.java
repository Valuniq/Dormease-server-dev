package dormease.dormeasedev.domain.users.user.controller;

import dormease.dormeasedev.domain.users.user.dto.request.*;
import dormease.dormeasedev.domain.users.user.dto.response.FindLoginIdRes;
import dormease.dormeasedev.domain.users.user.dto.response.FindMyInfoRes;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
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
@RequestMapping("/api/v1/app/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 아이디 찾기", description = "로그인 아이디를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindLoginIdRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "재설정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping("/phoneNumber")
    public ResponseEntity<?> modifyPhoneNumber (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 ModifyPhoneNumberReq 참고해주세요.", required = true) @Valid @RequestBody ModifyPhoneNumberReq modifyPhoneNumberReq
    ) {
        return userService.modifyPhoneNumber(customUserDetails, modifyPhoneNumberReq);
    }

    // Description : 대표 식당 변경
    @Operation(summary = "메인 페이지 - 대표 식당 변경", description = "메인 페이지에서 보일 대표 식당을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> modifyRepresentativeRestaurant (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "대표 식당으로 지정할 식당 id를 입력해주세요.", required = true) @PathVariable Long restaurantId
    ) {
        return userService.modifyRepresentativeRestaurant(customUserDetails, restaurantId);
    }
}
