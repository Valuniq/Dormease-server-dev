package dormease.dormeasedev.domain.common.service;

import dormease.dormeasedev.domain.common.dto.response.ExistApplicationRes;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.roommate_temp_application.domain.repository.RoommateTempApplicationRepository;
import dormease.dormeasedev.domain.roommate_temp_application.service.RoommateTempApplicationService;
import dormease.dormeasedev.domain.school.domain.School;
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
public class CommonService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final RoommateTempApplicationRepository roommateTempApplicationRepository;
    private final RoommateApplicationRepository roommateApplicationRepository;
    private final ExitRequestmentRepository exitRequestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final RoommateTempApplicationService roommateTempApplicationService;

    // Description : 입사 / 룸메이트 / 퇴사 신청 여부
    public ResponseEntity<?> checkApplication(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();
        Resident resident = residentService.validateResidentByUser(user);

        // 입사 신청 여부
        boolean existDormitoryApplication = dormitoryApplicationRepository.existsByUserAndApplicationStatus(user, ApplicationStatus.NOW);

        // 룸메이트 임시 신청 여부
        RoommateTempApplication roommateTempApplication = resident.getRoommateTempApplication();
        boolean existRoommateTempApplication = roommateTempApplication != null; // null이 아니면 존재 (true)
        boolean isMaster = false;
        if (existRoommateTempApplication) {
            isMaster = roommateTempApplicationService.isMaster(roommateTempApplication, resident);
        }
        // 룸메이트 신청 여부
        boolean existRoommateApplication = resident.getRoommateApplication() != null;

        // 퇴사 신청 여부
        boolean existExitRequestment = exitRequestmentRepository.existsByResident(resident);
        boolean existRoom = resident.getRoom() != null; // null이 아니면 존재 (true)

        ExistApplicationRes existApplicationRes = ExistApplicationRes.builder()
                .existDormitoryApplication(existDormitoryApplication)
                .userType(user.getUserType())
                .existRoommateTempApplication(existRoommateTempApplication)
                .isMaster(isMaster)
                .existRoommateApplication(existRoommateApplication)
                .existExitRequestment(existExitRequestment)
                .existRoom(existRoom)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(existApplicationRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
