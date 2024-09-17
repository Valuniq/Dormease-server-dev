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
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.StudentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import dormease.dormeasedev.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;
    private final SchoolService schoolService;

    @Transactional
    public void signUp(SignUpReq signUpReq) {
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
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .build();
        User saveUser = userRepository.save(user);

        Student student = Student.builder()
                .user(saveUser)
                .phoneNumber(signUpReq.getPhoneNumber())
                .studentNumber(signUpReq.getStudentNumber())
                .gender(signUpReq.getGender())
                /**
                 *  TODO : MSI 연동
                 *      .schoolStatus()
                 *      .address()
                 *      .major()
                 *      .schoolYear()
                 *      .grade()
                 */
                .build();
        studentRepository.save(student);
    }

    @Transactional
    public SignInRes signIn(SignInReq signInReq) {
        String loginId = signInReq.getLoginId();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디로 유저를 찾을 수 없습니다: " + loginId));
        String password = signInReq.getPassword();

        // 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginId, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return SignInRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(user.getUserType())
                .userName(user.getName())
                .build();
    }

    @Transactional
    public void signout(UserDetailsImpl userDetailsImpl) {
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByLoginId(userDetailsImpl.getLoginId());
        DefaultAssert.isTrue(findRefreshToken.isPresent(), "이미 로그아웃 되었습니다");

        refreshTokenRepository.delete(findRefreshToken.get());
    }

    public CheckLoginIdRes checkLoginId(String loginId) {
        Optional<User> finduser = userRepository.findByLoginId(loginId);
        boolean isDuplicate = false;
        if (finduser.isPresent())
            isDuplicate = true;

        return CheckLoginIdRes.builder()
                .isDuplicate(isDuplicate)
                .build();
    }
}
