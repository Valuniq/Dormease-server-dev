package dormease.dormeasedev.domain.dormitory_applications.term.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.repository.TermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.response.TermNameRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.service.SchoolService;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
public class TermService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final TermRepository termRepository;
    private final DormitoryRepository dormitoryRepository;

    private final SchoolService schoolService;
    private final UserService userService;

    // Description : 거주 기간 목록 조회
    public ResponseEntity<?> findTerms(UserDetailsImpl userDetailsImpl) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "입사 신청 기간이 아닙니다.");
        DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

        List<Term> termList = termRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<TermNameRes> termNameResList = new ArrayList<>();
        for (Term term : termList) {
            TermNameRes termNameRes = TermNameRes.builder()
                    .termId(term.getId())
                    .termName(term.getTermName())
                    .build();
            termNameResList.add(termNameRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(termNameResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public Term validateTermId(Long termId) {
        Optional<Term> findTerm = termRepository.findById(termId);
        DefaultAssert.isTrue(findTerm.isPresent(), "거주 기간 정보가 올바르지 않습니다.");
        return findTerm.get();
    }
}
