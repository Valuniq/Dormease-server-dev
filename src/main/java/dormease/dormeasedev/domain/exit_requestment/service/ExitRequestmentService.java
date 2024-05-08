package dormease.dormeasedev.domain.exit_requestment.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ExitRequestmentRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExitRequestmentService {

    private final ExitRequestmentRepository exitRequestmentRepository;

    private final UserService userService;

    public ResponseEntity<?> findResidents(CustomUserDetails customUserDetails) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        List<ExitRequestment> exitRequestmentList = exitRequestmentRepository.findAllBySchool(school);
        List<ExitRequestmentRes> exitRequestmentResList = new ArrayList<>();
        for (ExitRequestment exitRequestment : exitRequestmentList) {
            Resident resident = exitRequestment.getResident();
            User user = resident.getUser();
            Room room = resident.getRoom();
            Dormitory dormitory = room.getDormitory();

            ExitRequestmentRes exitRequestmentRes = ExitRequestmentRes.builder()
                    .refundRequestmentId(exitRequestment.getId())
                    .residentName(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .dormitoryName(dormitory.getName())
                    .roomSize(dormitory.getRoomSize())
                    .roomNumber(room.getRoomNumber())
                    .exitDate(exitRequestment.getExitDate())
                    .hasKey(exitRequestment.getHasKey())
                    .createDate(exitRequestment.getCreatedDate().toLocalDate())
                    .isReturnSecurityDeposit(exitRequestment.getIsReturnSecurityDeposit())
                    .build();
            exitRequestmentResList.add(exitRequestmentRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(exitRequestmentResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
