package dormease.dormeasedev.domain.roommates.roommate_application.service;

import dormease.dormeasedev.domain.roommates.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.RoommateApplicationResult;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.service.RoommateTempApplicationService;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoommateApplicationService {

    private final RoommateApplicationRepository roommateApplicationRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final RoommateTempApplicationService roommateTempApplicationService;

    // Description : 룸메이트 신청
    @Transactional
    public ResponseEntity<?> applyRoommateTempApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);

        RoommateTempApplication roommateTempApplication = roommateTempApplicationService.validateRoommateTempApplicationByResident(resident);
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
}
