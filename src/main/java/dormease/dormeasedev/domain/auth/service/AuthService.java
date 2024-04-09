package dormease.dormeasedev.domain.auth.service;

import dormease.dormeasedev.domain.auth.domain.RefreshToken;
import dormease.dormeasedev.domain.auth.domain.repository.RefreshTokenRepository;
import dormease.dormeasedev.domain.auth.dto.SignInRequest;
import dormease.dormeasedev.domain.auth.dto.SignInResponse;
import dormease.dormeasedev.domain.auth.dto.SignUpRequest;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.config.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public void signUp(SignUpRequest userSignUpDto) throws Exception {

        if (userRepository.findByLoginId(userSignUpDto.getLoginId()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .loginId(userSignUpDto.getLoginId())
                .password(userSignUpDto.getPassword())
                .name(userSignUpDto.getName())
                .userType(UserType.USER) // 관리자는 직접 만들어 줄 것이기 떄문
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public SignInResponse signIn(SignInRequest request) {

        User user = userRepository.findByLoginId(request.getLoginId())
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", user.getLoginId(), user.getUserType())); // Access Token 생성
        String refreshToken = tokenProvider.createRefreshToken(); // Refresh Token 생성

        // 리프레시 토큰이 이미 있으면 토큰을 갱신하고 없으면 토큰을 추가
        refreshTokenRepository.findById(user.getLoginId()) // loginId가 PK(@Id)이므로 : findById() 사용 !
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getLoginId(), user, refreshToken))
                );
        return new SignInResponse(user.getName(), user.getUserType(), accessToken, refreshToken);
    }
}
