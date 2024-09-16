package dormease.dormeasedev.domain.users.user.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.dto.response.ActiveUserInfoRes;
import dormease.dormeasedev.domain.users.user.dto.response.DeleteUserInfoRes;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.PageInfo;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // TODO: admin user 분리 시 변경사항 적용
    // 전체 회원 조회 및 정렬
    // Description: 기본(sortBy: createdDate / isAscending: false)
    public ResponseEntity<?> getActiveUsers(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());

        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<User> pagedUsers = userRepository.findBySchoolAndStatusAndUserTypeNot(admin.getSchool(), Status.ACTIVE, UserType.ADMIN, pageable);

        List<ActiveUserInfoRes> userInfos = pagedUsers.getContent().stream()
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

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, pagedUsers);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, userInfos);

        // ApiResponse 객체 생성하여 결과 반환
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 검색 회원 조회 및 정렬
    // Description: 기본(sortBy: createdDate / isAscending: false)
    // TODO: admin user 분리 시 변경사항 반영
    public ResponseEntity<?> getSearchActiveUsers(CustomUserDetails customUserDetails, String keyword, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        // 공백 제거
        String cleanedKeyword = keyword.trim().toLowerCase();;
        // 검색 결과 조회 및 페이징
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<User> searchResultPage = userRepository.searchUsersByKeyword(
                admin.getSchool(), cleanedKeyword, Status.ACTIVE, UserType.ADMIN, pageable);

        List<ActiveUserInfoRes> searchUsers = searchResultPage.getContent().stream()
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

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, searchResultPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, searchUsers);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 탈퇴 회원 목록 조회
    // 회원 탈퇴 시 이름, 학번, 전화번호, 상점, 벌점, 탈퇴날짜만 남길 것
    // 추후 재가입 시 학번 같으면 상/벌점 연결?
    public ResponseEntity<?> getDeleteUserBySchool(CustomUserDetails customUserDetails,Integer page) {
        User admin = validUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<User> users = userRepository.findBySchoolAndStatusAndUserTypeNot(admin.getSchool(), Status.DELETE, UserType.ADMIN, pageable);

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

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, users);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, deleteUserInfoRes);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    // 검색
    public ResponseEntity<?> searchDeleteUsers(CustomUserDetails customUserDetails, String keyword, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        // 공백 제거
        String cleanedKeyword = keyword.trim();
        // 검색 결과 조회 및 페이징
        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.DESC, "createdDate"));
        // 이름 또는 학번에 검색어가 포함된 사용자를 검색
        Page<User> searchResult = userRepository.searchUsersByKeyword(admin.getSchool(), cleanedKeyword, Status.DELETE, UserType.ADMIN, pageable);

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

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, searchResult);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, searchUsers);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
