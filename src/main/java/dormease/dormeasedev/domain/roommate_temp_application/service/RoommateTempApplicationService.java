package dormease.dormeasedev.domain.roommate_temp_application.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.service.DormitoryApplicationService;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.roommate_application.domain.RoommateApplicationResult;
import dormease.dormeasedev.domain.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.roommate_temp_application.domain.repository.RoommateTempApplicationRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoommateTempApplicationService {

    private final RoommateTempApplicationRepository roommateTempApplicationRepository;
    private final RoommateApplicationRepository roommateApplicationRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 룸메이트 임시 신청 생성
    @Transactional
    public ResponseEntity<?> createRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        DefaultAssert.isTrue(!roommateTempApplicationRepository.existsByRoommateMasterId(resident.getId()), "해당 사생의 그룹이 이미 존재합니다.");

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


    // Description : 룸메이트 신청
    public ResponseEntity<?> applyRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = validateRoommateTempApplicationByResident(resident);
        roommateTempApplication.updateIsApplied();

        // 룸메이트 신청 생성
        RoommateApplication roommateApplication = RoommateApplication.builder()
                .roommateApplicationResult(RoommateApplicationResult.WAITING)
                .build();

        roommateApplicationRepository.save(roommateApplication);

        List<Resident> residents = roommateTempApplication.getResidents();
        for (Resident residentIn : residents)
            residentIn.updateRoommateApplication(roommateApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("룸메이트 신청이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 코드 입력 후 신청하기 버튼 (그룹 참가)
    public ResponseEntity<?> joinRoommateTempApplication(CustomUserDetails customUserDetails, String code) {

        // 본인
        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);
        DormitoryApplication myDormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(user, ApplicationStatus.NOW);
        DormitoryTerm myDormitoryTerm = myDormitoryApplication.getDormitoryTerm();
        Dormitory myDormitory = myDormitoryTerm.getDormitory();
        Integer myRoomSize = myDormitory.getRoomSize();

        RoommateTempApplication roommateTempApplication = validateRoommateTempApplicationByCode(code);
        // 방장
        Long roommateMasterId = roommateTempApplication.getRoommateMasterId();
        Resident roommateMasterResident = residentService.validateResidentById(roommateMasterId);
        User roommateMasterUser = roommateMasterResident.getUser();
        DormitoryApplication dormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(roommateMasterUser, ApplicationStatus.NOW);
        DormitoryTerm dormitoryTerm = dormitoryApplication.getDormitoryTerm();
        Dormitory dormitory = dormitoryTerm.getDormitory();
        Integer roomSize = dormitory.getRoomSize();

        DefaultAssert.isTrue(!myDormitory.equals(dormitory), "신청한 기숙사가 해당 그룹의 방장의 신청 기숙사와 일치하지 않습니다.");
        DefaultAssert.isTrue(roommateTempApplication.getResidents().size() >= roomSize, "인원이 가득 찬 그룹입니다.");

        resident.addRoommateTempApplication(roommateTempApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("코드 입력(그룹 참가)이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 그룹 나가기
    public ResponseEntity<?> outOfRoommateTempApplication(CustomUserDetails customUserDetails, String code) {

        // 본인
        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = validateRoommateTempApplicationByResident(resident);
        resident.removeRoommateTempApplication(roommateTempApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("그룹 나가기가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // Description : 코드 생성 함수 (00000000 ~ 99999999) (8자리)
    private String generateCode() {

        SecureRandom random = new SecureRandom();
        // nextInt(int bound) : 0(포함)부터 입력된 bound(미포함) 사이의 랜덤 정수를 반환
        int randomNumber = random.nextInt(100000000);

        // 숫자를 8자리 문자열로 포맷팅
        // %08d는 8자리의 정수 / 부족한 자리는 0으로 채워짐
        String code = String.format("%08d", randomNumber);

        return code;
    }
    
    // Description : 유효성 검증 함수
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

}
