package dormease.dormeasedev.domain.auth.controller;

import dormease.dormeasedev.domain.auth.dto.SignInRequest;
import dormease.dormeasedev.domain.auth.dto.SignInResponse;
import dormease.dormeasedev.domain.auth.dto.SignUpRequest;
import dormease.dormeasedev.domain.auth.service.AuthService;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
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
    public String signUp(@RequestBody SignUpRequest userSignUpDto) throws Exception {
        authService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInRequest.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInResponse.class))}),
    })
    @PostMapping("/sign-in")
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        return authService.signIn(signInRequest);
    }

}