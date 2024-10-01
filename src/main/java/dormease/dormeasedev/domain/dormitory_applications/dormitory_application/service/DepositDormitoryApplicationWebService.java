package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DepositDormitoryApplicaionRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationWebRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DepositDormitoryApplicationWebService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;

    private final UserService userService;

    public List<DepositDormitoryApplicaionRes> findDepositDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.DEPOSIT)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        List<DormitoryApplication> depositDormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndDormitoryApplicationResultIn(
                        dormitoryApplicationSetting,
                        Arrays.asList(DormitoryApplicationResult.PASS, DormitoryApplicationResult.MOVE_PASS)
                );
        List<DepositDormitoryApplicaionRes> depositDormitoryApplicaionResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : depositDormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
            DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
            Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
            RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(applicationDormitory.getName())
                    .roomSize(applicationRoomType.getRoomSize())
                    .build();

            DepositDormitoryApplicaionRes depositDormitoryApplicaionRes = DepositDormitoryApplicaionRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(student.getUser().getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
                    .dormitoryApplicationResult(dormitoryApplication.getDormitoryApplicationResult())
                    .build();
            depositDormitoryApplicaionResList.add(depositDormitoryApplicaionRes);
        }
        return depositDormitoryApplicaionResList;
    }
}
