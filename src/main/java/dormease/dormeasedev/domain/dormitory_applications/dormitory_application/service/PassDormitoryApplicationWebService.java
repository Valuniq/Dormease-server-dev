package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationDormitoryRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassAllDormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassDormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.exception.InvalidSchoolAuthorityException;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PassDormitoryApplicationWebService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;

    private final UserService userService;

    public PassAllDormitoryApplicationRes findPassDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.PASS)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

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
                    .roommateCode(null) // TODO : 룸메이트 로직 수정 후 가능
                    .build();
            passDormitoryApplicationResList.add(passDormitoryApplicationRes);
        }
        return PassAllDormitoryApplicationRes.builder()
                .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                .passDormitoryApplicationResList(passDormitoryApplicationResList)
                .build();
    }

    public List<PassDormitoryApplicationRes> searchPassDormitoryApplications(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId, String searchWord) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        List<DormitoryApplication> dormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndDepositPaidAndStudent_StudentNumberContainingOrStudent_User_NameContaining(dormitoryApplicationSetting, true, searchWord, searchWord);
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
                    .roommateCode(null) // TODO : 룸메이트 로직 수정 후 가능
                    .build();
            passDormitoryApplicationResList.add(passDormitoryApplicationRes);
        }
        return passDormitoryApplicationResList;
    }

    public List<DormitoryApplicationDormitoryRes> findDormitoriesByDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        List<DormitorySettingTerm> dormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        // Set을 사용하여 중복을 방지
        Set<Long> dormitoryIds = new HashSet<>();
        List<DormitoryApplicationDormitoryRes> dormitoryApplicationDormitoryResList = new ArrayList<>();
        for (DormitorySettingTerm dormitorySettingTerm : dormitorySettingTermList) {
            DormitoryRoomType dormitoryRoomType = dormitorySettingTerm.getDormitoryRoomType();
            Dormitory dormitory = dormitoryRoomType.getDormitory();
            // 이미 처리된 dormitoryId는 무시
            if (dormitoryIds.contains(dormitory.getId()))
                continue;
            dormitoryIds.add(dormitory.getId());

            DormitoryApplicationDormitoryRes dormitoryApplicationDormitoryRes = DormitoryApplicationDormitoryRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    .build();
            dormitoryApplicationDormitoryResList.add(dormitoryApplicationDormitoryRes);
        }
        return dormitoryApplicationDormitoryResList;
    }

    public List<PassDormitoryApplicationRes> findPassDormitoryApplicationsByDormitory(UserDetailsImpl userDetailsImpl, Long dormitoryId) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.PASS)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        List<DormitoryApplication> dormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndDepositPaidAndResultDormitoryTerm_DormitoryRoomType_Dormitory_id(dormitoryApplicationSetting, true, dormitoryId);

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
                    .roommateCode(null) // TODO : 룸메이트 로직 수정 후 가능
                    .build();
            passDormitoryApplicationResList.add(passDormitoryApplicationRes);
        }
        return passDormitoryApplicationResList;
    }

    public List<PassDormitoryApplicationRes> searchPassDormitoryApplicationsByDormitory(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId, String searchWord, Long dormitoryId) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        List<DormitoryApplication> dormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndDepositPaidAndResultDormitoryTerm_DormitoryRoomType_Dormitory_idAndStudent_StudentNumberContainingOrStudent_User_NameContaining(dormitoryApplicationSetting, true, dormitoryId, searchWord, searchWord);
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
                    .roommateCode(null) // TODO : 룸메이트 로직 수정 후 가능
                    .build();
            passDormitoryApplicationResList.add(passDormitoryApplicationRes);
        }
        return passDormitoryApplicationResList;
    }
}

