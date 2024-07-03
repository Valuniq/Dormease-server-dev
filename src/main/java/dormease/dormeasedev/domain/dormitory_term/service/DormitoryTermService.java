package dormease.dormeasedev.domain.dormitory_term.service;

import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term.dto.response.DormitoryTermNameRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.service.SchoolService;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryTermService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitoryRepository dormitoryRepository;

    private final SchoolService schoolService;
    private final UserService userService;

    // Description : 거주 기간 목록 조회
    public ResponseEntity<?> findDormitoryTerms(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "입사 신청 기간이 아닙니다.");
        DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

        List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryTermNameRes> dormitoryTermNameResList = new ArrayList<>();
        for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
            DormitoryTermNameRes dormitoryTermNameRes = DormitoryTermNameRes.builder()
                    .dormitoryTermId(dormitoryTerm.getId())
                    .term(dormitoryTerm.getTerm())
                    .build();
            dormitoryTermNameResList.add(dormitoryTermNameRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryTermNameResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public DormitoryTerm validateDormitoryTermId(Long dormitoryTermId) {
        Optional<DormitoryTerm> findDormitoryTerm = dormitoryTermRepository.findById(dormitoryTermId);
        DefaultAssert.isTrue(findDormitoryTerm.isPresent(), "거주 기간 정보가 올바르지 않습니다.");
        return findDormitoryTerm.get();
    }
}
