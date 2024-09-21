package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationWebRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.StudentRepository;
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
public class DormitoryApplicationWebService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final StudentRepository studentRepository;

    private final UserService userService;

    // TODO : N+1 최적화
    public List<DormitoryApplicationWebRes> findDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findTopBySchoolAndApplicationStatusOrderByStartDateDesc(school, ApplicationStatus.BEFORE)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryApplicationWebRes> dormitoryApplicationWebResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            User user = student.getUser();

            DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
            DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
            Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
            RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(applicationDormitory.getName())
                    .roomSize(applicationRoomType.getRoomSize())
                    .build();

            DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes;
            DormitoryApplicationResult dormitoryApplicationResult = dormitoryApplication.getDormitoryApplicationResult();
            if (dormitoryApplicationResult.equals(DormitoryApplicationResult.WAIT))
                resultDormitoryRoomTypeRes = null;

            DormitoryTerm resultDormitoryTerm = dormitoryApplication.getResultDormitoryTerm();
            DormitoryRoomType resultDormitoryRoomType = resultDormitoryTerm.getDormitoryRoomType();
            Dormitory resultDormitory = resultDormitoryRoomType.getDormitory();
            RoomType resultRoomType = resultDormitoryRoomType.getRoomType();
            resultDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(resultDormitory.getName())
                    .roomSize(resultRoomType.getRoomSize())
                    .build();

            DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(user.getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
                    .address(student.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
                    .dormitoryApplicationResult(dormitoryApplication.getDormitoryApplicationResult())
                    .build();

            dormitoryApplicationWebResList.add(dormitoryApplicationWebRes);
        }

        return dormitoryApplicationWebResList;
    }
}
