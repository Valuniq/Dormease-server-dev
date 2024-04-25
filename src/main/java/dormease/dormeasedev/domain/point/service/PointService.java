package dormease.dormeasedev.domain.point.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.point.domain.Point;
import dormease.dormeasedev.domain.point.domain.PointType;
import dormease.dormeasedev.domain.point.domain.repository.PointRepository;
import dormease.dormeasedev.domain.point.dto.request.BonusPointManagementReq;
import dormease.dormeasedev.domain.point.dto.request.MinusPointManagementReq;
import dormease.dormeasedev.domain.point.dto.request.PointListReq;
import dormease.dormeasedev.domain.point.dto.response.PointRes;
import dormease.dormeasedev.domain.point.dto.response.UserInPointPageRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
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
    private final PointRepository pointRepository;


    // 상벌점 리스트 내역 조회
    public ResponseEntity<?> getPointList(CustomUserDetails customUserDetails) {
        User admin = validUserById(customUserDetails.getId());

        List<Point> points = pointRepository.findBySchoolAndStatus(admin.getSchool(), Status.ACTIVE);
        List<PointRes> pointResList = points.stream()
                .map(point -> PointRes.builder()
                        .id(point.getId())
                        .content(point.getContent())
                        .score(point.getScore())
                        .pointType(point.getPointType())
                        .build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pointResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 상벌점 리스트 내역 등록
    @Transactional
    public ResponseEntity<?> registerPointList(CustomUserDetails customUserDetails, PointListReq pointListReqs) {
        User admin = validUserById(customUserDetails.getId());
        List<BonusPointManagementReq> bonusPointList = pointListReqs.getBonusPointList();
        List<MinusPointManagementReq> minusPointList = pointListReqs.getMinusPointList();

        List<Point> bonusPoint = bonusPointList.stream()
                .map(bonusPointManagementReq -> Point.builder()
                        .school(admin.getSchool())
                        .pointType(PointType.BONUS)
                        .content(bonusPointManagementReq.getContent())
                        .score(bonusPointManagementReq.getScore())
                        .build())
                .collect(Collectors.toList());

        List<Point> minusPoint = minusPointList.stream()
                .map(minusPointManagementReq -> Point.builder()
                        .school(admin.getSchool())
                        .pointType(PointType.MINUS)
                        .content(minusPointManagementReq.getContent())
                        .score(minusPointManagementReq.getScore())
                        .build())
                .collect(Collectors.toList());

        pointRepository.saveAll(bonusPoint);
        pointRepository.saveAll(minusPoint);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("상벌점 내역이 등록되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 상벌점 리스트 내역 삭제
    @Transactional
    public ResponseEntity<?> deletePoint(CustomUserDetails customUserDetails, Long pointId) {
        User admin = validUserById(customUserDetails.getId());

        Optional<Point> pointOpt = pointRepository.findById(pointId);
        DefaultAssert.isTrue(pointOpt.isPresent(), "상벌점 정보가 올바르지 않습니다.");
        Point point = pointOpt.get();

        // 리스트 삭제해도 사용자의 상벌점 내역 조회 가능, 따라서 soft delete로 구현
        point.updateStatus(Status.DELETE);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("상벌점 내역이 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    // Description : 상벌점 내역 중 사생 관련 기능

    // 상벌점 부여
    // 리스트 없으면 에러
    
    // 상벌점 내역 삭제
    // 상벌점 내역 조회

    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<UserInPointPageRes> userUserInPointPageResList = residents.getContent().stream()
                .map(resident -> UserInPointPageRes.builder()
                        .id(resident.getUser().getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getDormitorySettingTerm().getDormitory().getName() + "(" + resident.getDormitorySettingTerm().getDormitory().getRoomSize() + ")").build())
                .sorted(Comparator.comparing(UserInPointPageRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userUserInPointPageResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getSortedResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<UserInPointPageRes> userUserInPointPageResList = residents.getContent().stream()
                .map(resident -> UserInPointPageRes.builder()
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
                .information(userUserInPointPageResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;

        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.searchResidentsByKeyword(admin.getSchool(), cleanedKeyword, pageable);

        List<UserInPointPageRes> userUserInPointPageResList = residents.getContent().stream()
                .map(resident -> UserInPointPageRes.builder()
                        .id(resident.getUser().getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .bonusPoint(resident.getUser().getBonusPoint())
                        .minusPoint(resident.getUser().getMinusPoint())
                        .dormitory(resident.getDormitorySettingTerm().getDormitory().getName() + "(" + resident.getDormitorySettingTerm().getDormitory().getRoomSize() + ")").build())
                .sorted(Comparator.comparing(UserInPointPageRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userUserInPointPageResList).build();

        return ResponseEntity.ok(apiResponse);
    }

    private User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
