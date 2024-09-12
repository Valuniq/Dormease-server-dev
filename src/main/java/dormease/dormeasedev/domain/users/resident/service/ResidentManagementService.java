package dormease.dormeasedev.domain.users.resident.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.respository.RefundRequestmentRepository;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.resident.dto.request.ResidentPrivateInfoReq;
import dormease.dormeasedev.domain.users.resident.dto.response.*;
import dormease.dormeasedev.domain.users.user.domain.SchoolStatus;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import dormease.dormeasedev.infrastructure.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentManagementService {

    private final ResidentRepository residentRepository;
    private final DormitoryRepository dormitoryRepository;
    private final RoomRepository roomRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final UserService userService;
    private final ResidentService residentService;
    private final RefundRequestmentRepository refundRequestmentRepository;
    private final ExitRequestmentRepository exitRequestmentRepository;
    private final S3Uploader s3Uploader;

    // 사생 상세 조회
    public ResponseEntity<?> getResidentDetailInfo(CustomUserDetails customUserDetails, Long residentId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");

        ResidentPrivateInfoRes residentPrivateInfoRes = getResidentPrivateInfo(resident);
        ResidentDormitoryInfoRes residentDormitoryInfoRes = getResidentDormitoryInfo(resident);

        ResidentDetailInfoRes residentDetailInfoRes = ResidentDetailInfoRes.builder()
                .residentPrivateInfoRes(residentPrivateInfoRes)
                .residentDormitoryInfoRes(residentDormitoryInfoRes)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentDetailInfoRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 사생 상세 조회 - 개인정보
    private ResidentPrivateInfoRes getResidentPrivateInfo(Resident resident) {
        User user = resident.getUser();
        // 미회원 사생 고려
        if (user == null) {
            return ResidentPrivateInfoRes.builder()
                    .residentId(resident.getId())
                    .name(resident.getName())
                    .gender(resident.getGender())
                    .build();
        } else {
            DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatusAndDormitoryApplicationResult(user, ApplicationStatus.NOW, DormitoryApplicationResult.PASS);
            return ResidentPrivateInfoRes.builder()
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

    }

    // 사생 상세 조회 - 기숙사정보
    private ResidentDormitoryInfoRes getResidentDormitoryInfo(Resident resident) {
        DormitoryTerm dormitoryTerm = resident.getDormitoryTerm();
        Term term = dormitoryTerm.getTerm();
        DormitoryRoomType dormitoryRoomType = dormitoryTerm.getDormitoryRoomType();
        Dormitory dormitory = dormitoryRoomType.getDormitory();

        // 기숙사 정보
        if (dormitory == null) {
            return ResidentDormitoryInfoRes.builder()
                    .termName(term.getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .build();
        } else if (resident.getRoom() == null) {
            return ResidentDormitoryInfoRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    // .roomSize() 호실이 없으면 인실 가져올 수 없음
                    .termName(term.getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .build();
        } else {
            String[] roommateNames = getRoommateNames(resident);
            return ResidentDormitoryInfoRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(resident.getRoom().getRoomType().getRoomSize())
                    .roomNumber(resident.getRoom().getRoomNumber())
                    .bedNumber(resident.getBedNumber())
                    .termName(term.getTermName())
                    .isApplyRoommate(resident.getIsRoommateApplied() != null ? resident.getIsRoommateApplied() : null)
                    .roommateNames(roommateNames)
                    .build();
        }
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

                    User user = resident.getUser(); // 미배정 사생은 미회원 사생만 존재 가능
                    Room room = resident.getRoom();

                    // 미회원인지 아닌지 구분
                    if (user != null) {
                        studentNumber = user.getStudentNumber();
                        bonusPoint = user.getBonusPoint();
                        minusPoint = user.getMinusPoint();
                        schoolStatus = user.getSchoolStatus();
                        DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                        dormitoryName = dormitoryRoomType.getDormitory().getName();
                        roomSize = dormitoryRoomType.getRoomType().getRoomSize();
                        roomNumber = room.getRoomNumber();

                    } else {
                        if (room != null) {
                            DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                            dormitoryName = dormitoryRoomType.getDormitory().getName();
                            roomSize = dormitoryRoomType.getRoomType().getRoomSize();
                            roomNumber = room.getRoomNumber();
                        }
                        // TODO : 사생 직접 추가 시 건물도 필수로 할지 기획과 논의 후 결정
//                        else if (dormitory != null) {
//                            dormitoryName = dormitory.getName();
//                        }
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

                    User user = resident.getUser(); // 미배정 사생은 미회원 사생만 존재 가능
                    Room room = resident.getRoom();

                    // 미회원인지 아닌지 구분
                    if (user != null) {
                        studentNumber = user.getStudentNumber();
                        bonusPoint = user.getBonusPoint();
                        minusPoint = user.getMinusPoint();
                        schoolStatus = user.getSchoolStatus();
                        DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                        dormitoryName = dormitoryRoomType.getDormitory().getName();
                        roomSize = dormitoryRoomType.getRoomType().getRoomSize();
                        roomNumber = room.getRoomNumber();

                    } else {
                        if (room != null) {
                            DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                            dormitoryName = dormitoryRoomType.getDormitory().getName();
                            roomSize = dormitoryRoomType.getRoomType().getRoomSize();
                            roomNumber = room.getRoomNumber();
                        }
                        // TODO : 사생 직접 추가 시 건물도 필수로 할지 기획과 논의 후 결정
//                        else if (dormitory != null) {
//                            dormitoryName = dormitory.getName();
//                        }
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

    // 사생 직접 추가 버튼 -> term 정보 보내줘야 함

    //
    // 이름 성별 term 필수
    // @Transactional
    // public ResponseEntity<?> addNewResident() {
        // 필수: school, name, gender term
        // 선택: hasKey
        // 무조건 null: user, dormitory, room, roommateTempApplication, roommateApplication, bedNumber, isRoommateApplied

    // }

    // 사생 정보 수정
    // Description : emergencyContact, emergencyRelation, bankName, accountNumber, dormitoryPayment, hasKey는 정보 수정 시 모두 보내줘야 함(변경되지 않은 값이 있더라도)
    // null을 보내는 건지 변경을 안하는 건지 구분 못함
    // Description : copy, prioritySelectionCopy는 변경 없을 시 보내지 말 것
    @Transactional
    public ResponseEntity<?> updateResidentPrivateInfo(CustomUserDetails customUserDetails, Long residentId,
                                                       Optional<MultipartFile> copy, Optional<MultipartFile>  prioritySelectionCopy, ResidentPrivateInfoReq residentPrivateInfoReq) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");

        String emergencyContact = residentPrivateInfoReq.getEmergencyContact();
        String emergencyRelation = residentPrivateInfoReq.getEmergencyRelation();
        String bankName = residentPrivateInfoReq.getBankName();
        String accountNumber = residentPrivateInfoReq.getAccountNumber();
        Boolean dormitoryPayment = residentPrivateInfoReq.getDormitoryPayment();
        // 미회원 사생 고려
        if (resident.getUser() != null) {
            // user가 있으면 입사신청을 했을 것이므로
            DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatusAndDormitoryApplicationResult(resident.getUser(), ApplicationStatus.NOW, DormitoryApplicationResult.PASS);
            if (copy.isPresent()) {
                uploadCopyFile(dormitoryApplication, copy.get());
            } else if (prioritySelectionCopy.isPresent()) {
                uploadPrioritySelectionCopyFile(dormitoryApplication, prioritySelectionCopy.get());
            }
            dormitoryApplication.updateResidentPrivateInfo(emergencyContact, emergencyRelation, bankName, accountNumber, dormitoryPayment);
        }
        resident.updateHasKey(residentPrivateInfoReq.getHasKey());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("사생의 정보가 수정되었습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private void uploadCopyFile(DormitoryApplication dormitoryApplication, MultipartFile file) {
        String originalFile = dormitoryApplication.getCopy().split("amazonaws.com/")[1];
        s3Uploader.deleteFile(originalFile);

        String imageUrl = s3Uploader.uploadImage(file);
        dormitoryApplication.updateCopy(imageUrl);
    }

    private void uploadPrioritySelectionCopyFile(DormitoryApplication dormitoryApplication, MultipartFile file) {
        String originalFile = dormitoryApplication.getPrioritySelectionCopy().split("amazonaws.com/")[1];
        s3Uploader.deleteFile(originalFile);

        String imageUrl = s3Uploader.uploadImage(file);
        dormitoryApplication.updatePrioritySelectionCopy(imageUrl);
    }


    // 기숙사 정보 수정
    // 호실, 침대번호는 NULL값 허용
    // 호실 골랐는데 성별 안 맞으면 오류뱉어
    // 호실, 침대번호는 숫자만 입력 가능
    // 호실 수정 시 자동으로 비어있는 침대번호가 배정되도록 함

    // 사생의 성별에 맞는 건물 조회
    // 빈 자리가 없는 건물은 드롭다운 메뉴에 뜨지 않음
    public ResponseEntity<?> getDormitoriesByGender(CustomUserDetails customUserDetails, Long residentId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");
        // resident가 속한 학교의 기숙사 리스트
        // 이미 배정된 건물 제외
        List<Dormitory> sameSchoolDormitories = dormitoryRepository.findBySchool(resident.getSchool());

        List<Dormitory> findDormitories = sameSchoolDormitories.stream()
                // 성별 조건을 만족하는 기숙사 필터링
                .filter(dormitory -> dormitoryRoomTypeRepository.existsByDormitoryAndRoomTypeGender(dormitory, resident.getGender()))
                // 수용인원 체크
                .filter(dormitory -> {
                    Integer currentPeopleCount = calculateCurrentPeopleCount(dormitory);
                    Integer dormitorySize = calculateDormitorySize(dormitory);
                    return currentPeopleCount < dormitorySize;
                })
                .toList();

        List<DormitoryResidentAssignmentRes> dormitoryResidentAssignmentRes = new ArrayList<>();

        for (Dormitory dormitory : findDormitories) {
            List<DormitoryRoomType> dormitoryRoomTypes = dormitoryRoomTypeRepository.findByDormitoryAndRoomTypeGender(dormitory, resident.getGender());
            // dormitoryRoomType별로 DormitoryResidentAssignmentRes 생성
            for (DormitoryRoomType dormitoryRoomType : dormitoryRoomTypes) {
                Integer roomSize = dormitoryRoomType.getRoomType().getRoomSize();
                dormitoryResidentAssignmentRes.add(
                        DormitoryResidentAssignmentRes.builder()
                                .dormitoryId(dormitory.getId())
                                .dormitoryName(dormitory.getName())
                                .roomSize(roomSize)
                                .build()
                );
            }
        }
        // 기숙사 이름, 인실로 정렬
        dormitoryResidentAssignmentRes.sort(Comparator.comparing(DormitoryResidentAssignmentRes::getDormitoryName)
                .thenComparing(DormitoryResidentAssignmentRes::getRoomSize));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryResidentAssignmentRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 현재 거주 인원
    public Integer calculateCurrentPeopleCount(Dormitory dormitory) {
        Integer currentPeopleCount = 0;
        List<Room> rooms = roomRepository.findByDormitoryAndIsActivated(dormitory, true);
        for (Room room : rooms) {
            currentPeopleCount += Optional.ofNullable(room.getCurrentPeople()).orElse(0);
        }
        return currentPeopleCount;
    }

    // 수용인원
    public Integer calculateDormitorySize(Dormitory dormitory) {
        return Optional.ofNullable(dormitory.getDormitorySize()).orElse(0);
    }

    // 사생 건물 재배치
    // TODO: 피그마 디자인보고 수정(거주기간 선택을 위해 입사신청설정 -> 기숙사 -> 거주기간 순서(예정))
    // @Transactional
    // public ResponseEntity<?> reassignResidentToDormitory(CustomUserDetails customUserDetails, Long residentId, Long dormitoryId) {
    //     User admin = userService.validateUserById(customUserDetails.getId());
    //     Resident resident = residentService.validateResidentById(residentId);
    //     DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");

        // 사생의 호실 및 침대번호 초기화
    //     Room room = resident.getRoom();
    //     if (room != null) {
    //         resident.updateRoom(null);
    //         resident.updateBedNumber(null);
            // room의 현재 거주인원 변경
    //         room.adjustRoomCurrentPeople(room, -1);
    //     }

        // 사생의 건물 업데이트
    //     Dormitory dormitory = dormitoryService.validateDormitoryId(dormitoryId);
    //     DefaultAssert.isTrue(dormitory.getGender() == resident.getGender(), "건물과 사생의 성별이 일치하지 않습니다.");
    //     resident.updateDormitory(dormitory);

    //     ApiResponse apiResponse = ApiResponse.builder()
    //             .check(true)
    //             .information(Message.builder().message("건물이 재배치되었습니다.").build()).build();

    //     return ResponseEntity.ok(apiResponse);
    // }

    // 호실 배치시 인원 없으면 없다고 띄우기
    // 배치 되면 인원 업데이트
    //// 맞춰서 기숙사 정보 업데이트(룸메이트)

    @Transactional
    public ResponseEntity<?> deleteResident(CustomUserDetails customUserDetails, Long residentId) {
        return processResidentExit(customUserDetails, residentId, UserType.USER, "퇴사 처리되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> addBlackList(CustomUserDetails customUserDetails, Long residentId) {
        return processResidentExit(customUserDetails, residentId, UserType.BLACKLIST, "블랙리스트로 추가되었습니다.");
    }

    private ResponseEntity<?> processResidentExit(CustomUserDetails customUserDetails, Long residentId, UserType userType, String message) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentById(residentId);
        DefaultAssert.isTrue(admin.getSchool() == resident.getSchool(), "관리자와 사생의 학교가 일치하지 않습니다.");
        User user = resident.getUser();

        // 환불 신청서 존재하면 예외
        DefaultAssert.isTrue(!refundRequestmentRepository.existsByResident(resident), "아직 해당 사생의 환불 신청서가 존재합니다.");
        // 퇴사 신청서 존재하면 예외
        DefaultAssert.isTrue(!exitRequestmentRepository.existsByResident(resident), "아직 해당 사생의 퇴사 신청서가 존재합니다.");

        // room 정보 변경
        Room room = resident.getRoom();
        if (room != null) {
            room.adjustRoomCurrentPeople(room, -1);
        }

        // 사생 데이터 삭제
        residentRepository.delete(resident);
        if (user != null) {
            user.updateUserType(userType);
            DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatusAndDormitoryApplicationResult(user, ApplicationStatus.NOW, DormitoryApplicationResult.PASS);
            // 입사신청 상태 변경
            if (dormitoryApplication != null) {
                dormitoryApplication.updateApplicationStatus(ApplicationStatus.BEFORE);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message(message).build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}