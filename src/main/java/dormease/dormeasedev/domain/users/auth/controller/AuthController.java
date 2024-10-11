package dormease.dormeasedev.domain.users.auth.controller;

import dormease.dormeasedev.domain.users.auth.dto.request.JoinReq;
import dormease.dormeasedev.domain.users.auth.dto.request.ModifyReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.users.auth.service.AuthService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/join")
    public ResponseEntity<?> join(
            @RequestParam(value = "key") String key,
            @Valid @RequestBody JoinReq joinReq
    ) {
        authService.join(joinReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PutMapping("/modify")
    public ResponseEntity<?> modify(
            @RequestParam(value = "key") String key,
            @Valid @RequestBody ModifyReq modifyReq
    ) {
        authService.modify(modifyReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq) {
        authService.signUp(signUpReq);
        return ResponseEntity.created(URI.create("/api/v1/app/users")).build();
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInReq signInReq) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authService.signIn(signInReq))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PostMapping(value = "/sign-out")
    public ResponseEntity<?> signout(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        authService.signout(userDetailsImpl);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<ApiResponse> checkLoginId(@PathVariable(value = "loginId") String loginId) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authService.checkLoginId(loginId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}