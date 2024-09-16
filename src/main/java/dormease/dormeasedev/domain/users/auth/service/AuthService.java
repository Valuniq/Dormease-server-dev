package dormease.dormeasedev.domain.users.auth.service;

import dormease.dormeasedev.domain.restaurants.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.repository.RestaurantRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.service.SchoolService;
import dormease.dormeasedev.domain.users.auth.domain.RefreshToken;
import dormease.dormeasedev.domain.users.auth.domain.repository.RefreshTokenRepository;
import dormease.dormeasedev.domain.users.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.users.auth.dto.response.CheckLoginIdRes;
import dormease.dormeasedev.domain.users.auth.dto.response.SignInRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.security.jwt.TokenProvider;
import dormease.dormeasedev.global.exception.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final UserService userService;
    private final SchoolService schoolService;

    @Transactional
    public ResponseEntity<?> signUp(SignUpReq signUpReq) {

        School school = schoolService.validateSchoolById(signUpReq.getSchoolId());
        // 이미 존재하는 로그인 아이디가 있으면
        DefaultAssert.isTrue(userRepository.findByLoginId(signUpReq.getLoginId()).isEmpty(), "중복된 아이디입니다.");

        Optional<Restaurant> findRestaurant = restaurantRepository.findTopBySchoolOrderByIdDesc(school);
        DefaultAssert.isTrue(findRestaurant.isPresent(), "대표 식당으로 지정할 식당이 해당 학교에 존재하지 않습니다.");
        Restaurant restaurant = findRestaurant.get();

        User user = User.builder()
                .school(school)
                .restaurant(restaurant)
                .loginId(signUpReq.getLoginId())
                .password(signUpReq.getPassword())
                .name(signUpReq.getName())
                .phoneNumber(signUpReq.getPhoneNumber())
                .studentNumber(signUpReq.getStudentNumber())
                .gender(signUpReq.getGender())
                .userType(UserType.USER) // 관리자는 직접 만들어 줄 것이기 떄문
                .bonusPoint(0)
                .minusPoint(0)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("회원가입이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
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

        SignInRes signInRes = SignInRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(user.getUserType())
                .userName(user.getName())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(signInRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> signout(CustomUserDetails customUserDetails) {

        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByLoginId(customUserDetails.getLoginId());
        DefaultAssert.isTrue(findRefreshToken.isPresent(), "이미 로그아웃 되었습니다");

        refreshTokenRepository.delete(findRefreshToken.get());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("로그아웃 되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> checkLoginId(String loginId) {

        Optional<User> finduser = userRepository.findByLoginId(loginId);
        boolean isDuplicate = false;
        if (finduser.isPresent())
            isDuplicate = true;

        CheckLoginIdRes checkLoginIdRes = CheckLoginIdRes.builder()
                .isDuplicate(isDuplicate)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(checkLoginIdRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
