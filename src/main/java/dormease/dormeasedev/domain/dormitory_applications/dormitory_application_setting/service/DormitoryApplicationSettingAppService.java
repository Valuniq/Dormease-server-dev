package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.service;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.period.dto.response.PeriodDateRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationSettingAppService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;

    private final UserService userService;

    public ResponseEntity<?> validateDormitoryApplicationPeriod(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        boolean isPeriod = dormitoryApplicationSettingRepository.existsBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
        PeriodDateRes periodDateRes = PeriodDateRes.builder()
                .isPeriod(isPeriod)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(periodDateRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
