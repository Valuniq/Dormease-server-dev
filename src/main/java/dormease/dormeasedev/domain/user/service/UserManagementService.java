package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.response.AllUserInfoBySchoolRes;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserManagementService {

    private final UserRepository userRepository;

    // 회원 목록 조회
    public ResponseEntity<?> getAllUserBySchool(CustomUserDetails customUserDetails) {
        User admin = validateUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchool(admin.getSchool());

        List<AllUserInfoBySchoolRes> allUserInfoBySchoolRes = users.stream()
                .map(user -> AllUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allUserInfoBySchoolRes).build();
        return  ResponseEntity.ok(apiResponse);
    }

    // 정렬?
    // 탈퇴 회원 목록 조회
    // 검색

    // 블랙리스트 사유 작성
    // 블랙리스트 목록 조회
    // 블랙리스트 삭제

    public User validateUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
