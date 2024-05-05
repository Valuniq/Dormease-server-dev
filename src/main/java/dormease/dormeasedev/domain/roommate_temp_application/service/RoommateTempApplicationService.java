package dormease.dormeasedev.domain.roommate_temp_application.service;

import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
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
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoommateTempApplicationService {

    private final RoommateTempApplicationRepository roommateTempApplicationRepository;

    private final UserService userService;
    private final ResidentService residentService;

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

        resident.updateRoommateTempApplication(roommateTempApplication);

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

        Optional<RoommateTempApplication> findRoommateTempApplication = roommateTempApplicationRepository.findByRoommateMasterId(resident.getId());
        DefaultAssert.isTrue(findRoommateTempApplication.isPresent(), "그룹이 존재하지 않습니다.");
        RoommateTempApplication roommateTempApplication = findRoommateTempApplication.get();
        
        roommateTempApplicationRepository.delete(roommateTempApplication);

        resident.updateRoommateTempApplication(null);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("그룹 삭제가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 룸메이트 신청
//    public ResponseEntity<?> applyRoommateTempApplication(CustomUserDetails customUserDetails) {
//
//        User user = userService.validateUserById(customUserDetails.getId());
//        Resident resident = residentService.validateResidentByUser(user);
//
//
//    }


    // Description : 코드 생성 함수 (00000000 ~ 99999999) (8자리)
    public String generateCode() {

        SecureRandom random = new SecureRandom();
        // nextInt(int bound) : 0(포함)부터 입력된 bound(미포함) 사이의 랜덤 정수를 반환
        int randomNumber = random.nextInt(100000000);

        // 숫자를 8자리 문자열로 포맷팅
        // %08d는 8자리의 정수 / 부족한 자리는 0으로 채워짐
        String code = String.format("%08d", randomNumber);

        return code;
    }


}
