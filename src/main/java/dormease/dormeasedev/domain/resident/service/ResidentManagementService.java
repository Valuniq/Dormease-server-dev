package dormease.dormeasedev.domain.resident.service;

import dormease.dormeasedev.domain.point.dto.response.ResidentInfoRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.resident.dto.response.ResidentRes;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentManagementService {

    private final ResidentRepository residentRepository;
    private final UserService userService;

    // 사생 직접 추가
    // 사생 정보 수정
    // 사생의 성별에 맞는 건물 조회
    // 사생 상세 조회

    // 사생 목록 조회
    // TODO : 설정한 퇴사 날짜 자정이 되기 10분 전에 해당 기간의 사생 데이터는 삭제
    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, Integer page) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<ResidentRes> residentResList = residents.getContent().stream()
                .map(resident -> ResidentRes.builder()
                        .residentId(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .gender(resident.getUser().getGender())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getRoom().getDormitory().getName() + "(" + resident.getRoom().getDormitory().getRoomSize() + "인실)")
                        .room(resident.getRoom().getRoomNumber())
                        .schoolStatus(resident.getUser().getSchoolStatus())
                        .build())
                .sorted(Comparator.comparing(ResidentRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getSortedResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = userService.validateUserById(customUserDetails.getId());
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<ResidentRes> residentResList = residents.getContent().stream()
                .map(resident -> ResidentRes.builder()
                        .residentId(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .gender(resident.getUser().getGender())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getRoom().getDormitory().getName() + "(" + resident.getRoom().getDormitory().getRoomSize() + "인실)")
                        .room(resident.getRoom().getRoomNumber())
                        .schoolStatus(resident.getUser().getSchoolStatus())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, Integer page) {
        User admin = userService.validateUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;

        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.searchResidentsByKeyword(admin.getSchool(), cleanedKeyword, pageable);

        List<ResidentRes> residentResList = residents.getContent().stream()
                .map(resident -> ResidentRes.builder()
                        .residentId(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .gender(resident.getUser().getGender())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getRoom().getDormitory().getName() + "(" + resident.getRoom().getDormitory().getRoomSize() + "인실)")
                        .room(resident.getRoom().getRoomNumber())
                        .schoolStatus(resident.getUser().getSchoolStatus())
                        .build())
                .sorted(Comparator.comparing(ResidentRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentResList).build();

        return ResponseEntity.ok(apiResponse);
    }
    
    // 퇴사 처리
    // 블랙리스트 추가
}
