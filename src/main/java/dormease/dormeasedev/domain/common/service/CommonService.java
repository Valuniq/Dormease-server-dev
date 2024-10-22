package dormease.dormeasedev.domain.common.service;

import dormease.dormeasedev.domain.common.dto.response.ExistApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.repository.StudentRepository;
import dormease.dormeasedev.domain.users.student.exception.StudentNotFoundException;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommonService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final RoommateApplicationRepository roommateApplicationRepository;
    private final ExitRequestmentRepository exitRequestmentRepository;
    private final ResidentRepository residentRepository;
    private final StudentRepository studentRepository;

    private final UserService userService;
    private final ResidentService residentService;

    // Description : 입사 / 룸메이트 / 퇴사 신청 여부
    public ResponseEntity<?> checkApplication(UserDetailsImpl userDetailsImpl) {
        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(StudentNotFoundException::new);
        Optional<Resident> resident = residentRepository.findByStudent(student);

        // 입사 신청 여부
        boolean existDormitoryApplication = dormitoryApplicationRepository.existsByStudentAndApplicationStatus(student, ApplicationStatus.NOW);

        // 룸메이트 신청 여부
        RoommateApplication roommateApplication = student.getRoommateApplication();
        boolean existRoommateApplication = roommateApplication != null; // null이 아니면 존재 (true)
        boolean isMaster = false;
        if (existRoommateApplication)
            isMaster = roommateApplication.getRoommateMasterId().equals(student.getId());

        // 룸메이트 신청 여부
        boolean isApplied = false;
        if (existRoommateApplication)
            isApplied = roommateApplication.isApplied();

        // 퇴사 신청 여부
        boolean existExitRequestment = false;
        if (resident.isPresent())
            existExitRequestment = exitRequestmentRepository.existsByResident(resident.get());
        boolean existRoom = false;
        if (resident.isPresent())
            existRoom =  resident.get().getRoom() != null; // null이 아니면 존재 (true)

        ExistApplicationRes existApplicationRes = ExistApplicationRes.builder()
                .existDormitoryApplication(existDormitoryApplication)
                .userType(user.getUserType())
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
