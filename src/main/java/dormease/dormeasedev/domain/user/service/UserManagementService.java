package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.response.ActiveUserInfoRes;
import dormease.dormeasedev.domain.user.dto.response.DeleteUserInfoRes;
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
        User admin = validUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatusAndUserTypeNotOrderByCreatedDateDesc(admin.getSchool(), Status.ACTIVE, UserType.ADMIN);

        List<ActiveUserInfoRes> activeUserInfoRes = users.stream()
                .map(user -> ActiveUserInfoRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate()) // User 객체의 createdDate 필드 사용
                        .build())
                // 최근 회원가입 한 순서로 정렬 (createdDate 기준 내림차순)
                .sorted(Comparator.comparing(ActiveUserInfoRes::getCreatedAt).reversed())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(activeUserInfoRes).build();
        return  ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> sortedUsers(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending) {
        User admin = validUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatusAndUserTypeNot(admin.getSchool(), Status.ACTIVE, UserType.ADMIN);

        List<ActiveUserInfoRes> sortedUsers = switch (sortBy) {
            case "BONUS" -> sortByBonusPoint(users, isAscending);
            case "MINUS" -> sortByMinusPoint(users, isAscending);
            case "CREATED_AT" -> sortByCreatedAt(admin, isAscending);
            default -> throw new DefaultException(ErrorCode.INVALID_CHECK, "유효하지 않은 정렬 기준입니다.");
        };

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(sortedUsers)
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    private List<ActiveUserInfoRes> sortByBonusPoint(List<User> users, boolean isAscending) {
        return users.stream()
                .map(user -> ActiveUserInfoRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .sorted(isAscending ?
                        Comparator.comparing(ActiveUserInfoRes::getBonusPoint) :
                        Comparator.comparing(ActiveUserInfoRes::getBonusPoint).reversed())
                .collect(Collectors.toList());
    }

    private List<ActiveUserInfoRes> sortByMinusPoint(List<User> users, boolean isAscending) {
        return users.stream()
                .map(user -> ActiveUserInfoRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .studentNumber(user.getStudentNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .bonusPoint(user.getBonusPoint())
                        .minusPoint(user.getMinusPoint())
                        .createdAt(user.getCreatedDate().toLocalDate())
                        .build())
                .sorted(isAscending ?
                        Comparator.comparing(ActiveUserInfoRes::getMinusPoint) :
                        Comparator.comparing(ActiveUserInfoRes::getMinusPoint).reversed())
                .collect(Collectors.toList());
    }

    private List<ActiveUserInfoRes> sortByCreatedAt(User admin, boolean isAscending) {
        if (!isAscending) {
            List<User> users = userRepository.findBySchoolAndStatusAndUserTypeNotOrderByCreatedDateDesc(admin.getSchool(), Status.ACTIVE, UserType.ADMIN);
            return users.stream()
                    .map(user -> ActiveUserInfoRes.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .bonusPoint(user.getBonusPoint())
                            .minusPoint(user.getMinusPoint())
                            .createdAt(user.getCreatedDate().toLocalDate())
                            .build())
                    .collect(Collectors.toList());
        } else {
            List<User> users = userRepository.findBySchoolAndStatusAndUserTypeNotOrderByCreatedDateAsc(admin.getSchool(), Status.ACTIVE, UserType.ADMIN);
            return users.stream()
                    .map(user -> ActiveUserInfoRes.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .bonusPoint(user.getBonusPoint())
                            .minusPoint(user.getMinusPoint())
                            .createdAt(user.getCreatedDate().toLocalDate())
                            .build())
                    .collect(Collectors.toList());
        }
    }


    // 검색
    public ResponseEntity<?> searchActiveUsers(CustomUserDetails customUserDetails, String keyword) {
        User admin = validUserById(customUserDetails.getId());
        // 공백 제거
        String cleanedKeyword = keyword.trim().toLowerCase();;
        // 이름 또는 학번에 검색어가 포함된 사용자를 검색, 생성일자 내림차순으로 정렬
        List<User> searchResult = userRepository.searchUsersByKeyword(
                admin.getSchool(), cleanedKeyword, Status.ACTIVE, UserType.ADMIN);

        List<ActiveUserInfoRes> searchUsers = searchResult.stream()
                .map(user -> ActiveUserInfoRes.builder()
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
                .information(searchUsers)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 탈퇴 회원 목록 조회
    // 회원 탈퇴 시 이름, 학번, 전화번호, 상점, 벌점, 탈퇴날짜만 남길 것
    // 추후 재가입 시 학번 같으면 상/벌점 연결?
    public ResponseEntity<?> getDeleteUserBySchool(CustomUserDetails customUserDetails) {
        User admin = validUserById(customUserDetails.getId());
        List<User> users = userRepository.findBySchoolAndStatusAndUserTypeNotOrderByCreatedDateDesc(admin.getSchool(), Status.DELETE, UserType.ADMIN);

        List<DeleteUserInfoRes> deleteUserInfoRes = users.stream()
                .map(user -> DeleteUserInfoRes.builder()
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
                .information(deleteUserInfoRes).build();
        return  ResponseEntity.ok(apiResponse);
    }

    // 검색
    public ResponseEntity<?> searchDeleteUsers(CustomUserDetails customUserDetails, String keyword) {
        User admin = validUserById(customUserDetails.getId());
        // 공백 제거
        String cleanedKeyword = keyword.trim();
        // 이름 또는 학번에 검색어가 포함된 사용자를 검색
        List<User> searchResult = userRepository.searchUsersByKeyword(
                admin.getSchool(), cleanedKeyword, Status.DELETE, UserType.ADMIN);

        List<DeleteUserInfoRes> searchUsers = searchResult.stream()
                .map(user -> DeleteUserInfoRes.builder()
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
                .information(searchUsers)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
