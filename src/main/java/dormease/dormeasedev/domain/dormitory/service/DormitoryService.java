package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.response.app_dormitory_application.FindDormitoryRes;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term.service.DormitoryTermService;
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
public class DormitoryService {

    private final DormitoryRepository dormitoryRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;

    private final SchoolService schoolService;
    private final UserService userService;

    // TODO : 거주 기간을 포함한 시작일 -> 마감일 존재하는 것들은 @Scheduled를 통해 기간 지나면 삭제하는 방안 생각
    
    // Description : APP - 입사 신청 중 기숙사 목록 조회 (본인 학교, 성별, 거주 기간에 따른 조회)
    public ResponseEntity<?> findDormitories(CustomUserDetails customUserDetails, String term) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // 거주 기간 이름으로 조회
        List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
        List<FindDormitoryRes> findDormitoryResList = new ArrayList<>();

        // 거주 기간에 해당되는 기숙사 모음
        for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
            // 가격 0원이면 입사 신청을 받지 않는 기숙사
            if (dormitoryTerm.getPrice() == 0)
                continue;
            Dormitory dormitory = dormitoryTerm.getDormitory();
            if (dormitory.getGender() == user.getGender()) {
                FindDormitoryRes findDormitoryRes = FindDormitoryRes.builder()
                        .dormitoryId(dormitory.getId())
                        .dormitoryName(dormitory.getName())
                        .roomSize(dormitory.getRoomSize())
                        .imageUrl(dormitory.getImageUrl())
                        .price(dormitoryTerm.getPrice())
                        .term(dormitoryTerm.getTerm())
                        .build();
                findDormitoryResList.add(findDormitoryRes);
            }
        }

        // 학교 -> 입사신청설정 (now) -> 기숙사 List -> 거주기간 List
//        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
//        List<DormitorySettingTerm> dormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
//        List<Dormitory> dormitoryList = new ArrayList<>();
//        for (DormitorySettingTerm dormitorySettingTerm : dormitorySettingTermList) {
//            // 기숙사 성별
//            if (dormitorySettingTerm.getDormitory().getGender() == user.getGender())
//                dormitoryList.add(dormitorySettingTerm.getDormitory());
//        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public Dormitory validateDormitoryId(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "기숙사 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }
}
