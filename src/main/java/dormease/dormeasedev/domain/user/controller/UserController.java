package dormease.dormeasedev.domain.user.controller;

import dormease.dormeasedev.domain.user.dto.request.FindLoginIdReq;
import dormease.dormeasedev.domain.user.dto.request.FindPasswordReq;
import dormease.dormeasedev.domain.user.dto.response.FindLoginIdRes;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
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

    @Operation(summary = "비밀번호 ", description = "비밀번호를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/password")
    public ResponseEntity<?> findPassword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 FindPasswordReq 참고해주세요.", required = true) @RequestBody FindPasswordReq findPasswordReq
    ) {
        return userService.findPassword(customUserDetails, findPasswordReq);
    }
}
