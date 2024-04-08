package dormease.dormeasedev.domain.auth.controller;

import dormease.dormeasedev.domain.auth.dto.SignInRequest;
import dormease.dormeasedev.domain.auth.dto.SignInResponse;
import dormease.dormeasedev.domain.auth.dto.SignUpRequest;
import dormease.dormeasedev.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignUpRequest userSignUpDto) throws Exception {
        authService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @PostMapping("/sign-in")
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        return authService.signIn(signInRequest);
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}