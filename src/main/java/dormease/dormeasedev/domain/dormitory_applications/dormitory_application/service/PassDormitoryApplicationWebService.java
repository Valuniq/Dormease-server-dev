package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassDormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PassDormitoryApplicationWebService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;

    private final UserService userService;

    public List<PassDormitoryApplicationRes> findPassDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.PASS)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        List<DormitoryApplication> dormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndDepositPaid(dormitoryApplicationSetting, true);
        List<PassDormitoryApplicationRes> passDormitoryApplicationResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            User user = student.getUser();

            PassDormitoryApplicationRes passDormitoryApplicationRes = PassDormitoryApplicationRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(user.getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .smoker(dormitoryApplication.getIsSmoking())
                    .roommateCode(null) // TODO : 임시. 여부였으면 좋겠음 / 여부 아니라면 코드를 구해야 함 / 룸메이트 로직 수정 후 가능
                    .build();
            passDormitoryApplicationResList.add(passDormitoryApplicationRes);
        }
        return passDormitoryApplicationResList;
    }
}
