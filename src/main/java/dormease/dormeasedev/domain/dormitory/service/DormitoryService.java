package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.app_dormitory_application.FindDormitoryReq;
import dormease.dormeasedev.domain.dormitory.dto.response.app_dormitory_application.FindDormitoryRes;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term_relation.domain.DormitoryTermRelation;
import dormease.dormeasedev.domain.dormitory_term_relation.domain.repository.DormitoryTermRelationRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.service.SchoolService;
import dormease.dormeasedev.global.DefaultAssert;
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
    private final DormitoryTermRelationRepository dormitoryTermRelationRepository;

    private final SchoolService schoolService;

    // Description : APP - 입사 신청 중 기숙사 목록 조회 (본인 학교, 성별, 거주 기간에 따른 조회)
    public ResponseEntity<?> findDormitories(FindDormitoryReq findDormitoryReq) {

        School school = schoolService.validateSchoolById(findDormitoryReq.getSchoolId());

        List<Dormitory> findDormitoryList = dormitoryRepository.findDormitoriesBySchoolIdAndGender(school.getId(), findDormitoryReq.getGender());

        List<FindDormitoryRes> findDormitoryResList = new ArrayList<>();

        for (Dormitory dormitory : findDormitoryList) {

            // 이전 입사 신청 설정에서의 기숙사는 조회 x
            List<DormitorySettingTerm> dormitorySettingTermList = dormitorySettingTermRepository.findByDormitory(dormitory);
            boolean flag = false;
            for (DormitorySettingTerm dormitorySettingTerm : dormitorySettingTermList) {
                if (!dormitorySettingTerm.getDormitoryApplicationSetting().getApplicationStatus().equals(ApplicationStatus.NOW)) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                flag = false;
                continue;
            }

            List<DormitoryTermRelation> findDormitoryTermRelation = dormitoryTermRelationRepository.findByDormitory(dormitory);

            for (DormitoryTermRelation dormitoryTermRelation : findDormitoryTermRelation) {
                DormitoryTerm dormitoryTerm = dormitoryTermRelation.getDormitoryTerm();

                if (dormitoryTerm.getTerm().equals(findDormitoryReq.getTerm())) {
                    FindDormitoryRes findDormitoryRes = FindDormitoryRes.builder()
                            .dormitoryId(dormitory.getId())
                            .dormitoryTermRelationId(dormitoryTermRelation.getId())
                            .dormitoryName(dormitory.getName())
                            .roomSize(dormitory.getRoomSize())
                            .imageUrl(dormitory.getImageUrl())
                            .price(dormitoryTerm.getPrice())
                            .build();
                    findDormitoryResList.add(findDormitoryRes);
                }
            }
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
