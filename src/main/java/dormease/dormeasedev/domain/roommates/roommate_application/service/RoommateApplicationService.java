package dormease.dormeasedev.domain.roommates.roommate_application.service;

import dormease.dormeasedev.domain.roommates.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.repository.RoommateApplicationRepository;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.repository.StudentRepository;
import dormease.dormeasedev.domain.users.student.exception.StudentNotFoundException;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoommateApplicationService {

    private final RoommateApplicationRepository roommateApplicationRepository;
    private final StudentRepository studentRepository;

    private final UserService userService;

    // Description : 룸메이트 신청
    @Transactional
    public void applyRoommate(UserDetailsImpl userDetailsImpl) {
        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(StudentNotFoundException::new);

        RoommateApplication roommateApplication = student.getRoommateApplication();
        roommateApplication.updateApplied(true);
    }
}
