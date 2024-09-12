package dormease.dormeasedev.domain.roommates.roommate_temp_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DormitoryApplicationService;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.domain.repository.RoommateTempApplicationRepository;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.dto.response.ExistRoommateTempApplicationRes;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.dto.response.RoommateTempApplicationMemberRes;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoommateTempApplicationService {

    private final RoommateTempApplicationRepository roommateTempApplicationRepository;
    private final RoommateApplicationRepository roommateApplicationRepository;
    private final ResidentRepository residentRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 룸메이트 임시 신청 여부 + 방장 여부 조회
    public ResponseEntity<?> existRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = resident.getRoommateTempApplication();

        Boolean existRoommateTempApplication = false;
        Boolean isMaster = false;
        if (roommateTempApplication != null) {
            existRoommateTempApplication = true; // null이 아니면 존재함
            isMaster = roommateTempApplication.getRoommateMasterId().equals(resident.getId());
        }

        ExistRoommateTempApplicationRes existRoommateTempApplicationRes = ExistRoommateTempApplicationRes.builder()
                .existRoommateTempApplication(existRoommateTempApplication)
                .isMaster(isMaster)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(existRoommateTempApplicationRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 룸메이트 임시 신청 생성
    @Transactional
    public ResponseEntity<?> createRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

//        DefaultAssert.isTrue(!roommateTempApplicationRepository.existsByRoommateMasterId(resident.getId()), "이미 그룹을 생성하였습니다.");
        DefaultAssert.isTrue(resident.getRoommateApplication() == null, "이미 소속된 그룹이 존재합니다.");

        String code;
        Optional<RoommateTempApplication> findRoommateTempApplication;
        do {
            code = generateCode();
            findRoommateTempApplication = roommateTempApplicationRepository.findByCode(code);
        } while (findRoommateTempApplication.isPresent());

        RoommateTempApplication roommateTempApplication = RoommateTempApplication.builder()
                .code(code)
                .isApplied(false)
                .roommateMasterId(resident.getId())
                .build();

        roommateTempApplicationRepository.save(roommateTempApplication);

        resident.addRoommateTempApplication(roommateTempApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("그룹 생성이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // Description : 룸메이트 임시 신청 삭제
    @Transactional
    public ResponseEntity<?> deleteRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = validateRoommateTempApplicationByResident(resident);

        roommateTempApplicationRepository.delete(roommateTempApplication);

        resident.updateRoommateTempApplication(null);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("그룹 삭제가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 코드 입력 후 신청하기 버튼 (그룹 참가)
    @Transactional
    public ResponseEntity<?> joinRoommateTempApplication(CustomUserDetails customUserDetails, String code) {

        // 본인
        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        DefaultAssert.isTrue(resident.getRoommateApplication() == null, "이미 소속된 그룹이 존재합니다.");

        // 나
        DormitoryApplication myDormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(user, ApplicationStatus.NOW);
        DormitoryTerm myResultDormitoryTerm = myDormitoryApplication.getResultDormitoryTerm();

        RoommateTempApplication roommateTempApplication = validateRoommateTempApplicationByCode(code);
        DefaultAssert.isTrue(!roommateTempApplication.getIsApplied(), "이미 신청이 확정된 그룹입니다.");

        // 방장
        Long roommateMasterId = roommateTempApplication.getRoommateMasterId();
        Resident roommateMasterResident = residentService.validateResidentById(roommateMasterId);
        User roommateMasterUser = roommateMasterResident.getUser();
        DormitoryApplication dormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(roommateMasterUser, ApplicationStatus.NOW);
        DormitoryTerm resultDormitoryTerm = dormitoryApplication.getResultDormitoryTerm();

        DefaultAssert.isTrue(myResultDormitoryTerm.equals(resultDormitoryTerm), "신청한 기숙사 혹은 거주 기간이 해당 그룹의 방장이 신청한 기숙사 혹은 거주 기간과 일치하지 않습니다.");

        Integer roomSize = myResultDormitoryTerm.getDormitoryRoomType().getRoomType().getRoomSize();
        DefaultAssert.isTrue(!(roommateTempApplication.getResidents().size() >= roomSize), "인원이 가득 찬 그룹입니다.");

        resident.addRoommateTempApplication(roommateTempApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("코드 입력(그룹 참가)이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 그룹 나가기
    @Transactional
    public ResponseEntity<?> outOfRoommateTempApplication(CustomUserDetails customUserDetails) {

        // 본인
        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = resident.getRoommateTempApplication();
        resident.removeRoommateTempApplication(roommateTempApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("그룹 나가기가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 그룹원 조회
    public ResponseEntity<?> findRoommateTempApplicationMembers(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = resident.getRoommateTempApplication();
        DefaultAssert.isTrue(roommateTempApplication != null, "룸메이트 그룹에 참가하지 않았습니다.");

        List<Resident> residentList = residentRepository.findByRoommateTempApplication(roommateTempApplication);
        List<RoommateTempApplicationMemberRes> roommateTempApplicationMemberResList = new ArrayList<>();
        for (Resident member : residentList) {
            User memberUser = member.getUser();
            RoommateTempApplicationMemberRes roommateTempApplicationMemberRes = RoommateTempApplicationMemberRes.builder()
                    .residentId(member.getId())
                    .name(memberUser.getName())
                    .studentNumber(memberUser.getStudentNumber())
                    .isMaster(isMaster(roommateTempApplication, member))
                    .code(roommateTempApplication.getCode())
                    .isApplied(roommateTempApplication.getIsApplied())
                    .build();
            roommateTempApplicationMemberResList.add(roommateTempApplicationMemberRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roommateTempApplicationMemberResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // Description : 코드 생성 함수 (00000000 ~ zzzzzzzz) (8자리)
    private String generateCode() {

        SecureRandom random = new SecureRandom();

        // nextLong(long bound) : 0(포함)부터 입력된 bound(미포함) 사이의 랜덤 정수를 반환
        long randomNumber = random.nextLong(2821109907455L + 1);
        String code = Long.toString(randomNumber, 36);

        code = String.format("%8s", code).replace(' ', '0');

        return code;
    }
    
    // Description : 유효성 검증 함수 - 내가 방장인 그룹 검증
    public RoommateTempApplication validateRoommateTempApplicationByResident(Resident resident) {
        Optional<RoommateTempApplication> findRoommateTempApplication = roommateTempApplicationRepository.findByRoommateMasterId(resident.getId());
        DefaultAssert.isTrue(findRoommateTempApplication.isPresent(), "본인이 생성한 그룹이 존재하지 않습니다.");
        return  findRoommateTempApplication.get();
    }

    public RoommateTempApplication validateRoommateTempApplicationByCode(String code) {
        Optional<RoommateTempApplication> findRoommateTempApplication = roommateTempApplicationRepository.findByCode(code);
        DefaultAssert.isTrue(findRoommateTempApplication.isPresent(), "해당 코드의 그룹이 존재하지 않습니다.");
        return  findRoommateTempApplication.get();
    }

    public Boolean isMaster(RoommateTempApplication roommateTempApplication, Resident resident) {
        return roommateTempApplication.getRoommateMasterId().equals(resident.getId());
    }

}
