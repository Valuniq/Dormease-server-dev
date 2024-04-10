package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.request.FindLoginIdReq;
import dormease.dormeasedev.domain.user.dto.response.FindLoginIdRes;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    // 아이디 찾기
    public ResponseEntity<?> findLoginId(CustomUserDetails customUserDetails, FindLoginIdReq findLoginIdReq) throws Exception {

        User user = validateUserById(customUserDetails.getId());

        String reqName = findLoginIdReq.getName();
        String reqPhoneNumber = findLoginIdReq.getPhoneNumber();

        DefaultAssert.isTrue(user.getName().equals(reqName), "이름이 일치하지 않습니다.");
        DefaultAssert.isTrue(user.getPhoneNumber().equals(reqPhoneNumber), "전화번호가 일치하지 않습니다.");

        FindLoginIdRes findLoginIdRes = FindLoginIdRes.builder()
                .loginId(user.getLoginId())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findLoginIdRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public User validateUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

}
