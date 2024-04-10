package dormease.dormeasedev.domain.auth.controller;

import dormease.dormeasedev.domain.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.auth.dto.response.CheckLoginIdRes;
import dormease.dormeasedev.domain.auth.dto.response.SignInRes;
import dormease.dormeasedev.domain.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.auth.service.AuthService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Auth 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody SignUpReq signUpReq
    ) {
        return authService.signUp(signUpReq);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInRes.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @RequestBody SignInReq signInReq
    ) {
        return authService.signIn(signInReq);
    }

    @Operation(summary = "로그인 아이디 중복 체크", description = "로그인 아이디가 중복인지 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 체크 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckLoginIdRes.class))}),
            @ApiResponse(responseCode = "400", description = "중복 체크 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<?> checkLoginId(
            @Parameter(description = "검사할 아이디를 입력해주세요.", required = true) @PathVariable(value = "loginId") String loginId
    ) {
        return authService.checkLoginId(loginId);
    }

}