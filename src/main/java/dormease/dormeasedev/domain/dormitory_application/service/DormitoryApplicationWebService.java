package dormease.dormeasedev.domain.dormitory_application.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application.dto.response.DormitoryApplicationUserRes;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationWebService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;

    private final UserService userService;
    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 현재 입사 신청자 목록 조회 / 페이징 x
    public ResponseEntity<?> findNowDormitoryApplicationList(CustomUserDetails customUserDetails) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "현재 입사 신청 설정이 존재하지 않습니다.");
        DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryApplicationUserRes> dormitoryApplicationUserResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            User user = dormitoryApplication.getUser();
            Dormitory dormitory = dormitoryApplication.getDormitory();
            Dormitory resultDormitory = dormitoryApplication.getResultDormitory();
            String resultDormitoryName = null;
            Integer resultDormitorySize = null;
            if (resultDormitory != null) {
                resultDormitoryName = resultDormitory.getName();
                resultDormitorySize = resultDormitory.getDormitorySize();
            }

            DormitoryApplicationUserRes dormitoryApplicationUserRes = DormitoryApplicationUserRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .name(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .gender(user.getGender())
                    .applicationDormitoryName(dormitory.getName())
                    .applicationDormitoryRoomSize(dormitory.getRoomSize())
                    .address(user.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryName(resultDormitoryName)
                    .resultDormitorySize(resultDormitorySize)
                    .dormitoryApplicationResult(dormitoryApplication.getDormitoryApplicationResult())
                    .build();
            dormitoryApplicationUserResList.add(dormitoryApplicationUserRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationUserResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 id로 신청자 명단 조회
    public ResponseEntity<?> findDormitoryApplicationList(CustomUserDetails customUserDetails, Long dormitoryApplicationSettingId, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "존재하지 않는 입사 신청 설정 id입니다.");
        DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.ASC, "createdDate"));

        Page<DormitoryApplication> dormitoryApplicationPage = dormitoryApplicationRepository.findDormitoryApplicaionsByDormitoryApplicationSetting(dormitoryApplicationSetting, pageable);
        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationPage.getContent();
        List<DormitoryApplicationUserRes> dormitoryApplicationUserResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            User user = dormitoryApplication.getUser();
            Dormitory dormitory = dormitoryApplication.getDormitory();
            Dormitory resultDormitory = dormitoryApplication.getResultDormitory();
            String resultDormitoryName = null;
            Integer resultDormitorySize = null;
            if (resultDormitory != null) {
                resultDormitoryName = resultDormitory.getName();
                resultDormitorySize = resultDormitory.getDormitorySize();
            }

            DormitoryApplicationUserRes dormitoryApplicationUserRes = DormitoryApplicationUserRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .name(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .gender(user.getGender())
                    .applicationDormitoryName(dormitory.getName())
                    .applicationDormitoryRoomSize(dormitory.getRoomSize())
                    .address(user.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryName(resultDormitoryName)
                    .resultDormitorySize(resultDormitorySize)
                    .dormitoryApplicationResult(dormitoryApplication.getDormitoryApplicationResult())
                    .build();
            dormitoryApplicationUserResList.add(dormitoryApplicationUserRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, dormitoryApplicationPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, dormitoryApplicationUserResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
