package dormease.dormeasedev.domain.exit_requestments.exit_requestment.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.request.ExitRequestmentReq;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response.ResidentInfoForExitRes;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
    public ResponseEntity<?> findInfoForExitRequestment(UserDetailsImpl userDetailsImpl) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Resident resident = residentService.validateResidentByUser(user);
        Student student = resident.getStudent();
        Room room = resident.getRoom();
        Dormitory dormitory = room.getDormitory();

        ResidentInfoForExitRes residentInfoForExitRes = ResidentInfoForExitRes.builder()
                .residentId(resident.getId())
                .residentName(user.getName())
                .studentNumber(student.getStudentNumber())
                .phoneNumber(student.getPhoneNumber())
                .major(student.getMajor())
                .schoolYear(student.getSchoolYear())
                .dormitoryName(dormitory.getName())
                .roomSize(room.getRoomType().getRoomSize())    // 수정
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
    public ResponseEntity<?> submitExitRequestment(UserDetailsImpl userDetailsImpl, ExitRequestmentReq exitRequestmentReq) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
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
