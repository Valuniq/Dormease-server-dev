package dormease.dormeasedev.domain.users.auth.controller;

import dormease.dormeasedev.domain.users.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.users.auth.service.AuthService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Auth API", description = "Auth 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody SignUpReq signUpReq
    ) {
        authService.signUp(signUpReq);
        return ResponseEntity.created(URI.create("/api/v1/app/users")).build();
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(
            @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @RequestBody SignInReq signInReq
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authService.signIn(signInReq))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PostMapping(value = "/sign-out")
    public ResponseEntity<?> signout(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        authService.signout(userDetailsImpl);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<ApiResponse> checkLoginId(
            @Parameter(description = "검사할 아이디를 입력해주세요.", required = true) @PathVariable(value = "loginId") String loginId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authService.checkLoginId(loginId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}