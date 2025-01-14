package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.app_dormitory_application.FindDormitoryRes;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.repository.TermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.service.TermService;
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
    public ResponseEntity<?> findDormitories(UserDetailsImpl userDetailsImpl, Long termId) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();

        Term term = termService.validateTermId(termId);
        List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
        List<FindDormitoryRes> findDormitoryResList = new ArrayList<>();
        for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
            if (dormitoryTerm.getPrice() == 0)
                continue;

            DormitoryRoomType dormitoryRoomType = dormitoryTerm.getDormitoryRoomType();
            Dormitory dormitory = dormitoryRoomType.getDormitory();
            RoomType roomType = dormitoryRoomType.getRoomType();
            FindDormitoryRes findDormitoryRes = FindDormitoryRes.builder()
                    .dormitoryTermId(dormitoryTerm.getId())
                    .imageUrl(dormitory.getImageUrl())
                    .dormitoryName(dormitory.getName())
                    .roomSize(roomType.getRoomSize())
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
