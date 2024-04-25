package dormease.dormeasedev.domain.point.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.point.dto.response.PointRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class PointService {

    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;

    // 상벌점 부여
    // 상벌점 목록 조회
    // 상벌점 내역 삭제
    // 상벌점 내역 조회
    // 상벌점 리스트 내역 관리

    // 사생 목록 조회
    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<PointRes> userPointResList = residents.getContent().stream()
                .map(resident -> PointRes.builder()
                        .id(resident.getUser().getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getDormitorySettingTerm().getDormitory().getName() + "(" + resident.getDormitorySettingTerm().getDormitory().getRoomSize() + ")").build())
                .sorted(Comparator.comparing(PointRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    // 정렬
    public ResponseEntity<?> getSortedResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<PointRes> userPointResList = residents.getContent().stream()
                .map(resident -> PointRes.builder()
                        .id(resident.getUser().getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getDormitorySettingTerm().getDormitory().getName() + "(" + resident.getDormitorySettingTerm().getDormitory().getRoomSize() + ")").build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    // 검색
    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;

        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.searchResidentsByKeyword(admin.getSchool(), cleanedKeyword, pageable);

        List<PointRes> userPointResList = residents.getContent().stream()
                .map(resident -> PointRes.builder()
                        .id(resident.getUser().getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getDormitorySettingTerm().getDormitory().getName() + "(" + resident.getDormitorySettingTerm().getDormitory().getRoomSize() + ")").build())
                .sorted(Comparator.comparing(PointRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    private User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
