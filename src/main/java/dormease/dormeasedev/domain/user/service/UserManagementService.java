package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.response.ActiveUserInfoBySchoolRes;
import dormease.dormeasedev.domain.user.dto.response.DeleteUserInfoBySchoolRes;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.error.DefaultException;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserManagementService {

    private final UserRepository userRepository;

    // 회원 목록 조회
    public ResponseEntity<?> getActiveUsers(CustomUserDetails customUserDetails) {
        User admin = validateUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatus(admin.getSchool(), Status.ACTIVE);

        List<ActiveUserInfoBySchoolRes> activeUserInfoBySchoolRes = users.stream()
                .map(user -> ActiveUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                // 최근 회원가입 한 순서로 정렬
                .sorted(Comparator.comparing(ActiveUserInfoBySchoolRes::getCreatedAt).reversed())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(activeUserInfoBySchoolRes).build();
        return  ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> sortedUsers(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending) {
        User admin = validateUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatus(admin.getSchool(), Status.ACTIVE);

        List<ActiveUserInfoBySchoolRes> sortedUsers = switch (sortBy) {
            case "BONUS" -> sortByBonusPoint(users, isAscending);
            case "MINUS" -> sortByMinusPoint(users, isAscending);
            case "CREATED_AT" -> sortByCreatedAt(users, isAscending);
            default -> throw new DefaultException(ErrorCode.INVALID_CHECK, "유효하지 않은 정렬 기준입니다.");
        };

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(sortedUsers)
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    private List<ActiveUserInfoBySchoolRes> sortByBonusPoint(List<User> users, boolean isAscending) {
        return users.stream()
                .map(user -> ActiveUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .sorted(isAscending ?
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getBonusPoint) :
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getBonusPoint).reversed())
                .collect(Collectors.toList());
    }

    private List<ActiveUserInfoBySchoolRes> sortByMinusPoint(List<User> users, boolean isAscending) {
        return users.stream()
                .map(user -> ActiveUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .sorted(isAscending ?
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getMinusPoint) :
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getMinusPoint).reversed())
                .collect(Collectors.toList());
    }

    private List<ActiveUserInfoBySchoolRes> sortByCreatedAt(List<User> users, boolean isAscending) {
        return users.stream()
                .map(user -> ActiveUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .sorted(isAscending ?
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getCreatedAt) :
                        Comparator.comparing(ActiveUserInfoBySchoolRes::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }


    // 검색


    // 탈퇴 회원 목록 조회
    // 회원 탈퇴 시 이름, 학번, 전화번호, 상점, 벌점, 탈퇴날짜만 남길 것
    // 추후 재가입 시 학번 같으면 상/벌점 연결?
    public ResponseEntity<?> getDeleteUserBySchool(CustomUserDetails customUserDetails) {
        User admin = validateUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatus(admin.getSchool(), Status.DELETE);

        List<DeleteUserInfoBySchoolRes> deleteUserInfoBySchoolRes = users.stream()
                .map(user -> DeleteUserInfoBySchoolRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .deletedAt(user.getModifiedDate().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(deleteUserInfoBySchoolRes).build();
        return  ResponseEntity.ok(apiResponse);
    }

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
