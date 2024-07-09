package dormease.dormeasedev.domain.resident.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.resident.dto.response.ResidentDormitoryInfoRes;
import dormease.dormeasedev.domain.resident.dto.response.ResidentPrivateInfoRes;
import dormease.dormeasedev.domain.resident.dto.response.ResidentRes;
import dormease.dormeasedev.domain.user.domain.SchoolStatus;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentManagementService {

    private final ResidentRepository residentRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final UserService userService;
    private final ResidentService residentService;

    // 사생 직접 추가
    // 사생 정보 수정
    // 사생의 성별에 맞는 건물 조회


    // 사생 상세 조회 - 개인정보
    public ResponseEntity<?> getResidentPrivateInfo(CustomUserDetails customUserDetails, Long residentId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");

        User user = resident.getUser();
        // 개인정보
        ResidentPrivateInfoRes residentPrivateInfoRes;
        // 미회원 사생 고려
        if (user == null) {
            residentPrivateInfoRes = ResidentPrivateInfoRes.builder()
                    .residentId(resident.getId())
                    .name(resident.getName())
                    .gender(resident.getGender())
                    .build();
        } else {
            DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatusAndDormitoryApplicationResult(user, ApplicationStatus.NOW, DormitoryApplicationResult.PASS);
            residentPrivateInfoRes = ResidentPrivateInfoRes.builder()
                    .residentId(resident.getId())
                    .name(resident.getName())
                    .major(user.getMajor())
                    .schoolYear(user.getSchoolYear())
                    .studentNumber(user.getStudentNumber())
                    .schoolStatus(user.getSchoolStatus())
                    .gender(resident.getGender())
                    .phoneNumber(user.getPhoneNumber())
                    .address(user.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .mealTicketCount(dormitoryApplication.getMealTicket().getCount())
                    .isSmoking(dormitoryApplication.getIsSmoking())
                    .dormitoryPayment(dormitoryApplication.getDormitoryPayment())
                    .hasKey(resident.getHasKey())
                    .bonusPoint(user.getBonusPoint())
                    .minusPoint(user.getMinusPoint())
                    .personalInfoConsent(true)
                    .thirdPartyConsent(true)
                    .bankName(dormitoryApplication.getBankName())
                    .accountNumber(dormitoryApplication.getAccountNumber())
                    .emergencyContact(dormitoryApplication.getEmergencyContact())
                    .emergencyRelation(dormitoryApplication.getEmergencyRelation())
                    .build();
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentPrivateInfoRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 사생 상세 조회 - 기숙사정보
    public ResponseEntity<?> getResidentDormitoryInfo(CustomUserDetails customUserDetails, Long residentId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");

        ResidentDormitoryInfoRes residentDormitoryInfoRes;
        Dormitory dormitory = resident.getDormitory();
        // 기숙사 정보
        if (dormitory == null) {
            residentDormitoryInfoRes = ResidentDormitoryInfoRes.builder()
                    .termName(resident.getTerm().getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .build();
        } else if (resident.getRoom() == null) {
            residentDormitoryInfoRes = ResidentDormitoryInfoRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(dormitory.getRoomSize())
                    .termName(resident.getTerm().getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .build();
        } else {
            String[] roommateNames = getRoommateNames(resident);
            residentDormitoryInfoRes = ResidentDormitoryInfoRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(dormitory.getRoomSize())
                    .roomNumber(resident.getRoom().getRoomNumber())
                    .bedNumber(resident.getBedNumber())
                    .termName(resident.getTerm().getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .roommateNames(roommateNames)
                    .build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentDormitoryInfoRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private String[] getRoommateNames(Resident resident) {
        List<Resident> residents = residentRepository.findByRoom(resident.getRoom());
        List<String> roommatesList = new ArrayList<>();
        for (Resident r : residents) {
            if (!r.getId().equals(resident.getId())) {
                roommatesList.add(r.getName());
            }
        }
        return roommatesList.toArray(new String[0]);
    }

    // 사생 목록 조회 및 정렬
    // Description : 기본(sortBy: Name / isAscending: true)
    // TODO : 설정한 퇴사 날짜 자정이 되기 10분 전에 해당 기간의 사생 데이터는 삭제
    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findBySchool(admin.getSchool(), pageable);

        // Description: 변경사항에 맞춰 로직 수정
        List<ResidentRes> residentResList = residents.getContent().stream()
                .map(resident -> {
                    String dormitoryName = null;
                    Integer roomSize = null;
                    Integer roomNumber = null;
                    String studentNumber = null;
                    Integer bonusPoint = 0;
                    Integer minusPoint = 0;
                    SchoolStatus schoolStatus = null;
                    // 미회원인지 아닌지 구분
                    if (resident.getUser() != null) {
                        studentNumber = resident.getUser().getStudentNumber();
                        bonusPoint = resident.getUser().getBonusPoint();
                        minusPoint = resident.getUser().getMinusPoint();
                        schoolStatus = resident.getUser().getSchoolStatus();
                    }
                    // 호실 배정이 있으면 건물도 무조건 존재
                    if (resident.getRoom() != null) {
                        roomNumber = resident.getRoom().getRoomNumber();
                        dormitoryName = resident.getDormitory().getName();
                        roomSize = resident.getDormitory().getRoomSize();
                    } else if (resident.getDormitory() != null) {
                        dormitoryName = resident.getDormitory().getName();
                        roomSize = resident.getDormitory().getRoomSize();
                    }

                    return ResidentRes.builder()
                            .residentId(resident.getId())
                            .name(resident.getName())
                            .studentNumber(studentNumber)
                            .gender(resident.getGender())
                            .bonusPoint(bonusPoint)
                            .minusPoint(minusPoint)
                            .dormitoryName(dormitoryName)
                            .roomSize(roomSize)
                            .roomNumber(roomNumber)
                            .schoolStatus(schoolStatus)
                            .build();
                })
                .collect(Collectors.toList());

        // 정렬
        Comparator<ResidentRes> comparator = switch (sortBy) {
            case "bonusPoint" ->
                    Comparator.comparing(ResidentRes::getBonusPoint, Comparator.nullsLast(Comparator.naturalOrder()));
            case "minusPoint" ->
                    Comparator.comparing(ResidentRes::getMinusPoint, Comparator.nullsLast(Comparator.naturalOrder()));
            case "dormitory" ->
                    Comparator.comparing(ResidentRes::getDormitoryName, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(ResidentRes::getRoomSize, Comparator.nullsLast(Comparator.naturalOrder()));
            case "gender" ->
                    Comparator.comparing(ResidentRes::getGender, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(ResidentRes::getName, Comparator.nullsLast(Comparator.naturalOrder()));
        };

        if (!isAscending) {
            comparator = comparator.reversed();
        }

        residentResList.sort(comparator);

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, residents);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, residentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse).build();

        return ResponseEntity.ok(apiResponse);
    }

    // 검색 및 정렬
    // Description : 기본(sortBy: Name / isAscending: true)
    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, String sortBy, Boolean isAscending, Integer page) {
        User admin = userService.validateUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;

        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.searchResidentsByKeyword(admin.getSchool(), cleanedKeyword, pageable);

        List<ResidentRes> residentResList = residents.getContent().stream()
                .map(resident -> {
                    String dormitoryName = null;
                    Integer roomSize = null;
                    Integer roomNumber = null;
                    String studentNumber = null;
                    Integer bonusPoint = 0;
                    Integer minusPoint = 0;
                    SchoolStatus schoolStatus = null;
                    // 미회원인지 아닌지 구분
                    if (resident.getUser() != null) {
                        studentNumber = resident.getUser().getStudentNumber();
                        bonusPoint = resident.getUser().getBonusPoint();
                        minusPoint = resident.getUser().getMinusPoint();
                        schoolStatus = resident.getUser().getSchoolStatus();
                    }
                    // 호실 배정이 있으면 건물도 무조건 존재
                    if (resident.getRoom() != null) {
                        roomNumber = resident.getRoom().getRoomNumber();
                        dormitoryName = resident.getDormitory().getName();
                        roomSize = resident.getDormitory().getRoomSize();
                    } else if (resident.getDormitory() != null) {
                        dormitoryName = resident.getDormitory().getName();
                        roomSize = resident.getDormitory().getRoomSize();
                    }

                    return ResidentRes.builder()
                            .residentId(resident.getId())
                            .name(resident.getName())
                            .studentNumber(studentNumber)
                            .gender(resident.getGender())
                            .bonusPoint(bonusPoint)
                            .minusPoint(minusPoint)
                            .dormitoryName(dormitoryName)
                            .roomSize(roomSize)
                            .roomNumber(roomNumber)
                            .schoolStatus(schoolStatus)
                            .build();
                })
                .collect(Collectors.toList());

        // 정렬
        Comparator<ResidentRes> comparator = switch (sortBy) {
            case "bonusPoint" ->
                    Comparator.comparing(ResidentRes::getBonusPoint, Comparator.nullsLast(Comparator.naturalOrder()));
            case "minusPoint" ->
                    Comparator.comparing(ResidentRes::getMinusPoint, Comparator.nullsLast(Comparator.naturalOrder()));
            case "dormitory" ->
                    Comparator.comparing(ResidentRes::getDormitoryName, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(ResidentRes::getRoomSize, Comparator.nullsLast(Comparator.naturalOrder()));
            case "gender" ->
                    Comparator.comparing(ResidentRes::getGender, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(ResidentRes::getName, Comparator.nullsLast(Comparator.naturalOrder()));
        };

        if (!isAscending) {
            comparator = comparator.reversed();
        }

        residentResList.sort(comparator);

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, residents);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, residentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse).build();

        return ResponseEntity.ok(apiResponse);
    }
    
    // 퇴사 처리
    // 블랙리스트 추가
    // userType user로 변경
}
