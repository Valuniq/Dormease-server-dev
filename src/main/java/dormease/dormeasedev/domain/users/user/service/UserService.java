package dormease.dormeasedev.domain.users.user.service;

import dormease.dormeasedev.domain.restaurants.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.repository.RestaurantRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.auth.domain.repository.RefreshTokenRepository;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.StudentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.dto.request.*;
import dormease.dormeasedev.domain.users.user.dto.response.FindLoginIdRes;
import dormease.dormeasedev.domain.users.user.dto.response.FindMyInfoRes;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RestaurantRepository restaurantRepository;
    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

    // Description : 아이디 찾기
    public ResponseEntity<?> findLoginId(UserDetailsImpl userDetailsImpl, FindLoginIdReq findLoginIdReq) {

        DefaultAssert.isTrue(findLoginIdReq.isCertification(), "인증번호가 잘못 입력되었습니다.");

        User user = validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 회원이 존재하지 않습니다."));

        String reqName = findLoginIdReq.getName();
        String reqPhoneNumber = findLoginIdReq.getPhoneNumber();

        DefaultAssert.isTrue(user.getName().equals(reqName), "이름이 일치하지 않습니다.");
        DefaultAssert.isTrue(student.getPhoneNumber().equals(reqPhoneNumber), "전화번호가 일치하지 않습니다.");

        FindLoginIdRes findLoginIdRes = FindLoginIdRes.builder()
                .loginId(user.getLoginId())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findLoginIdRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 비밀번호 재설정
    @Transactional
    public ResponseEntity<?> modifyPassword(UserDetailsImpl userDetailsImpl, FindPasswordReq findPasswordReq) {

        User user = validateUserById(userDetailsImpl.getUserId());
        DefaultAssert.isTrue(user.getLoginId().equals(findPasswordReq.getLoginId()), "아이디가 일치하지 않습니다.");

        user.updatePassword(passwordEncoder.encode(findPasswordReq.getPassword()));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("비밀번호가 변경되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 내 정보 조회
    public ResponseEntity<?> findMyInfo(UserDetailsImpl userDetailsImpl) {

        User user = validateUserById(userDetailsImpl.getUserId());
        boolean isBlackList = false;
        if (user.getUserType().getValue().equals("ROLE_BLACKLIST"))
            isBlackList = true;

        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 회원이 존재하지 않습니다."));
        FindMyInfoRes findMyInfoRes = FindMyInfoRes.builder()
                .loginId(user.getLoginId())
                .studentNumber(student.getStudentNumber())
                .phoneNumber(student.getPhoneNumber())
                .isBlackList(isBlackList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findMyInfoRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 학번(수험번호) 수정
    @Transactional
    public ResponseEntity<?> modifyStudentNumber(UserDetailsImpl userDetailsImpl, ModifyStudentNumberReq modifyStudentNumberReq) {

        User user = validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();
        validateUserByStudentNumber(school, modifyStudentNumberReq.getStudentNumber());

        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 회원이 존재하지 않습니다."));

        student.updateStudentNumber(modifyStudentNumberReq.getStudentNumber());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("학번 수정이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 비밀번호 재설정 - 마이페이지
    @Transactional
    public ResponseEntity<?> resetPasswordInMyPage(UserDetailsImpl userDetailsImpl, ResetPasswordReq resetPasswordReq) {

        User user = validateUserById(userDetailsImpl.getUserId());
        user.updatePassword(passwordEncoder.encode(resetPasswordReq.getPassword()));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("비밀번호가 변경되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전화번호 재설정
    @Transactional
    public ResponseEntity<?> modifyPhoneNumber(UserDetailsImpl userDetailsImpl, ModifyPhoneNumberReq modifyPhoneNumberReq) {

        User user = validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 회원이 존재하지 않습니다."));
        student.updatePhoneNumber(modifyPhoneNumberReq.getPhoneNumber());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("전화번호가 변경되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 대표 식당 변경
    @Transactional
    public ResponseEntity<?> modifyRepresentativeRestaurant(UserDetailsImpl userDetailsImpl, Long restaurantId) {

        User user = validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();
        Optional<Restaurant> findRestaurant = restaurantRepository.findBySchoolAndId(school, restaurantId);
        DefaultAssert.isTrue(findRestaurant.isPresent(), "존재하지 않는 식당 id입니다.");

        Restaurant newRestaurant = findRestaurant.get();
        Restaurant restaurant = user.getRestaurant();
        if (restaurant != null)
            DefaultAssert.isTrue(!restaurant.getId().equals(restaurantId), "이미 대표 식당으로 지정된 식당입니다.");

        user.updateRestaurant(newRestaurant);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("대표 식당이 변경되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public User validateUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }

    // Description : 학번 수정 위함 - 학번 중복되면 안되므로
    public void validateUserByStudentNumber(School school, String studentNumber) {
        Optional<User> findUser = userRepository.findBySchoolAndStudentNumber(school, studentNumber);
        DefaultAssert.isTrue(findUser.isEmpty(), "이미 가입된 학번입니다."); // 동일 학교 검증 추가 필요
    }

}
