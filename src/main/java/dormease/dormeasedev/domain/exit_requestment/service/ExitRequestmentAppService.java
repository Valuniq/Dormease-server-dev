package dormease.dormeasedev.domain.exit_requestment.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ResidentInfoForExitRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
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
                .roomSize(dormitory.getRoomSize())
                .roomNumber(room.getRoomNumber())
                .bedNumber(resident.getBedNumber())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentInfoForExitRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


}
