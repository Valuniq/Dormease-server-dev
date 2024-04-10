package dormease.dormeasedev.domain.auth.service;

import dormease.dormeasedev.domain.auth.domain.RefreshToken;
import dormease.dormeasedev.domain.auth.domain.repository.RefreshTokenRepository;
import dormease.dormeasedev.domain.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.auth.dto.response.CheckLoginIdRes;
import dormease.dormeasedev.domain.auth.dto.response.SignInRes;
import dormease.dormeasedev.domain.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.domain.repository.SchoolRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.config.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<?> signUp(SignUpReq signUpReq) throws Exception {

        Optional<School> findSchool = schoolRepository.findById(signUpReq.getSchoolId());

        if (findSchool.isEmpty())
            throw new Exception("존재하지 않는 학교입니다.");

        School school = findSchool.get();

        if (userRepository.findByLoginId(signUpReq.getLoginId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        User user = User.builder()
                .school(school)
                .name(signUpReq.getName())
                .gender(signUpReq.getGender())
                .phoneNumber(signUpReq.getPhoneNumber())
                .schoolNumber(signUpReq.getSchoolNumber())
                .loginId(signUpReq.getLoginId())
                .password(signUpReq.getPassword())
                .userType(UserType.USER) // 관리자는 직접 만들어 줄 것이기 떄문
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 완료");
    }

    @Transactional
    public ResponseEntity<?> signIn(SignInReq signInReq) {

        User user = userRepository.findByLoginId(signInReq.getLoginId())
                .filter(it -> passwordEncoder.matches(signInReq.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", user.getLoginId(), user.getUserType())); // Access Token 생성
        String refreshToken = tokenProvider.createRefreshToken(); // Refresh Token 생성

        // 리프레시 토큰이 이미 있으면 토큰을 갱신하고 없으면 토큰을 추가
        refreshTokenRepository.findById(user.getLoginId()) // loginId가 PK(@Id)이므로 : findById() 사용
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getLoginId(), user, refreshToken))
                );

        return ResponseEntity.ok(new SignInRes(accessToken, refreshToken, user.getUserType()));
    }

    public ResponseEntity<?> checkLoginId(String loginId) throws Exception {
        Optional<User> finduser = userRepository.findByLoginId(loginId);
        boolean isDuplicate = false;
        if (finduser.isPresent())
            isDuplicate = true;

        CheckLoginIdRes checkLoginIdRes = CheckLoginIdRes.builder()
                .isDuplicate(isDuplicate)
                .build();

        return ResponseEntity.ok(checkLoginIdRes);
    }
}
