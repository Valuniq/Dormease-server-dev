package dormease.dormeasedev.domain.exit_requestment.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestment.dto.request.ExitRequestmentReq;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ResidentInfoForExitRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.room.domain.Room;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExitRequestmentAppService {

    private final ExitRequestmentRepository exitRequestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;

    // Description : 퇴사 확인서를 위한 사생 정보 조회
    public ResponseEntity<?> findInfoForExitRequestment(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);
        Room room = resident.getRoom();
        Dormitory dormitory = room.getDormitory();

        ResidentInfoForExitRes residentInfoForExitRes = ResidentInfoForExitRes.builder()
                .residentId(resident.getId())
                .residentName(user.getName())
                .studentNumber(user.getStudentNumber())
                .phoneNumber(user.getPhoneNumber())
                .major(user.getMajor())
                .schoolYear(user.getSchoolYear())
                .dormitoryName(dormitory.getName())
                .roomSize(dormitory.getDormitoryRoomType().getRoomType().getRoomSize())    // TODO: 수정 필요
                .roomNumber(room.getRoomNumber())
                .bedNumber(resident.getBedNumber())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentInfoForExitRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 퇴사 확인서 제출
    @Transactional
    public ResponseEntity<?> submitExitRequestment(CustomUserDetails customUserDetails, ExitRequestmentReq exitRequestmentReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);
        DefaultAssert.isTrue(!exitRequestmentRepository.existsByResident(resident), "이미 퇴사 확인서를 제출하였습니다.");

        ExitRequestment exitRequestment = ExitRequestment.builder()
                .resident(resident)
                .exitDate(exitRequestmentReq.getExitDate())
                .hasKey(exitRequestmentReq.getHasKey())
                .keyNumber(exitRequestmentReq.getKeyNumber())
                .bankName(exitRequestmentReq.getBankName())
                .accountNumber(exitRequestmentReq.getAccountNumber())
                .build();

        exitRequestmentRepository.save(exitRequestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("퇴사 확인서 제출이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
