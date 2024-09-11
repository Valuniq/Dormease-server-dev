package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.response.app_dormitory_application.FindDormitoryRes;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term.service.DormitoryTermService;
import dormease.dormeasedev.domain.term.domain.Term;
import dormease.dormeasedev.domain.term.domain.repository.TermRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.service.SchoolService;
import dormease.dormeasedev.domain.term.service.TermService;
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
    private final TermRepository termRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryTermRepository dormitoryTermRepository;

    private final SchoolService schoolService;
    private final UserService userService;
    private final TermService termService;

    // TODO : 거주 기간을 포함한 시작일 -> 마감일 존재하는 것들은 @Scheduled를 통해 기간 지나면 삭제하는 방안 생각
    
    // Description : APP - 입사 신청 중 기숙사 목록 조회 (본인 학교, 성별, 거주 기간에 따른 조회)
    public ResponseEntity<?> findDormitories(CustomUserDetails customUserDetails, Long termId) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Term term = termService.validateTermId(termId);
        List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
        List<FindDormitoryRes> findDormitoryResList = new ArrayList<>();
        for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
            if (dormitoryTerm.getPrice() == 0)
                continue;
            Dormitory dormitory = dormitoryTerm.getDormitory();
            FindDormitoryRes findDormitoryRes = FindDormitoryRes.builder()
                    .dormitoryId(dormitory.getId())
                    .dormitoryName(dormitory.getName())
                    //.roomSize(dormitory.getRoomSize())    // TODO: 수정 필요
                    .imageUrl(dormitory.getImageUrl())
                    .price(dormitoryTerm.getPrice())
                    .termName(term.getTermName())
                    .build();
            findDormitoryResList.add(findDormitoryRes);
        }

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
