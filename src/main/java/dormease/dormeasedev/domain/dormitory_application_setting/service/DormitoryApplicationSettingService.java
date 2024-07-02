package dormease.dormeasedev.domain.dormitory_application_setting.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryReq;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryForFindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory.service.DormitoryService;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingHistoryRes;
import dormease.dormeasedev.domain.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term.dto.request.DormitoryTermReq;
import dormease.dormeasedev.domain.dormitory_term.dto.response.DormitoryTermRes;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.meal_ticket.dto.request.MealTicketReq;
import dormease.dormeasedev.domain.meal_ticket.dto.response.MealTicketRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class DormitoryApplicationSettingService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final MealTicketRepository mealTicketRepository;

    private final UserService userService;
    private final DormitoryService dormitoryService;

    // Description : 입사 신청 설정 생성
    //  - 입사 신청 설정으로 저장 필요
    //      - 그 과정에서 Req 참고하여 알맞은 Entity에 저장하는 과정 필요
    @Transactional
    public ResponseEntity<?> createDormitoryApplicationSetting(CustomUserDetails customUserDetails, CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // 입사 신청 설정 save
        DormitoryApplicationSetting dormitoryApplicationSetting = DormitoryApplicationSetting.builder()
                .school(school)
                .title(createDormitoryApplicationSettingReq.getTitle())
                .startDate(createDormitoryApplicationSettingReq.getStartDate())
                .endDate(createDormitoryApplicationSettingReq.getEndDate())
                .depositStartDate(createDormitoryApplicationSettingReq.getDepositStartDate())
                .depositEndDate(createDormitoryApplicationSettingReq.getDepositEndDate())
                .securityDeposit(createDormitoryApplicationSettingReq.getSecurityDeposit())
                .applicationStatus(ApplicationStatus.NOW) // 앞으로 사용할 것이기에 NOW
                .build();
        dormitoryApplicationSettingRepository.save(dormitoryApplicationSetting);

        // 식권 save : MealTicketReq - 삭권 관련 설정
        for (MealTicketReq mealTicketReq : createDormitoryApplicationSettingReq.getMealTicketReqList()) {
            MealTicket mealTicket = MealTicket.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .count(mealTicketReq.getCount())
                    .price(mealTicketReq.getPrice())
                    .build();
            mealTicketRepository.save(mealTicket);
        }

        // DormitoryReq - 기숙사 자체 설정 관련
        for (DormitoryReq dormitoryReq : createDormitoryApplicationSettingReq.getDormitoryReqList()) {
            Dormitory dormitory = dormitoryService.validateDormitoryId(dormitoryReq.getDormitoryId());

            // DormitorySettingTerm save - 기숙사 : 입사 신청 설정 M:N 관계 중간 테이블
            DormitorySettingTerm dormitorySettingTerm = DormitorySettingTerm.builder()
                    .dormitory(dormitory)
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .acceptLimit(dormitoryReq.getAcceptLimit())
                    .build();
            dormitorySettingTermRepository.save(dormitorySettingTerm);

            // DormitoryTerm(거주 기간) save
            for (DormitoryTermReq dormitoryTermReq : dormitoryReq.getDormitoryTermReqList()) {
                DormitoryTerm dormitoryTerm = DormitoryTerm.builder()
                        .dormitory(dormitory)
                        .term(dormitoryTermReq.getTerm())
                        .price(dormitoryTermReq.getPrice())
                        .startDate(dormitoryTermReq.getStartDate())
                        .endDate(dormitoryTermReq.getEndDate())
                        .build();
                dormitoryTermRepository.save(dormitoryTerm);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("입사 신청 설정이 완료되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 조회
    public ResponseEntity<?> findDormitoryApplicationSetting(CustomUserDetails customUserDetails, Long dormitoryApplicationSettingId) {

        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // 식권 조회
        List<MealTicketRes> mealTicketResList = new ArrayList<>();
        List<MealTicket> mealTicketList = mealTicketRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        for (MealTicket mealTicket : mealTicketList) {
            MealTicketRes mealTicketRes = MealTicketRes.builder()
                    .id(mealTicket.getId())
                    .count(mealTicket.getCount())
                    .price(mealTicket.getPrice())
                    .build();
            mealTicketResList.add(mealTicketRes);
        }

        // Dormitory(기숙사) + Dormitory Term(거주 기간) 조회
        List<DormitorySettingTerm> findDormitorySettingTermListByDormSettingTerm = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryForFindDormitoryApplicationSettingRes> dormitoryForFindDormitoryApplicationSettingResList = new ArrayList<>();

        for (DormitorySettingTerm dormitorySettingTerm : findDormitorySettingTermListByDormSettingTerm) {
            Dormitory dormitory = dormitorySettingTerm.getDormitory();

            // 거주 기간 조회
            List<DormitoryTerm> findDormitoryTerm = dormitoryTermRepository.findByDormitory(dormitory);
            List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();

            for (DormitoryTerm dormitoryTerm : findDormitoryTerm) {
                DormitoryTermRes dormitoryTermRes = DormitoryTermRes.builder()
                        .dormitoryTermId(dormitoryTerm.getId())
                        .term(dormitoryTerm.getTerm())
                        .price(dormitoryTerm.getPrice())
                        .startDate(dormitoryTerm.getStartDate())
                        .endDate(dormitoryTerm.getEndDate())
                        .build();
                dormitoryTermResList.add(dormitoryTermRes);
            }

            DormitoryForFindDormitoryApplicationSettingRes dormitoryForFindDormitoryApplicationSettingRes = DormitoryForFindDormitoryApplicationSettingRes.builder()
                    .dormitoryId(dormitory.getId())
                    // TODO : 수용 인원 맞는지 확인 필요
                    .dormitorySize(dormitory.getDormitorySize())
                    .dormitoryTermResList(dormitoryTermResList)
                    .build();
            dormitoryForFindDormitoryApplicationSettingResList.add(dormitoryForFindDormitoryApplicationSettingRes);
        }

        // 입사 신청 설정 조회
        FindDormitoryApplicationSettingRes findDormitoryApplicationSettingRes = FindDormitoryApplicationSettingRes.builder()
                .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                .title(dormitoryApplicationSetting.getTitle())
                .startDate(dormitoryApplicationSetting.getStartDate())
                .endDate(dormitoryApplicationSetting.getEndDate())
                .depositStartDate(dormitoryApplicationSetting.getDepositStartDate())
                .depositEndDate(dormitoryApplicationSetting.getDepositEndDate())
                .securityDeposit(dormitoryApplicationSetting.getSecurityDeposit())
                .applicationStatus(dormitoryApplicationSetting.getApplicationStatus())
                .dormitoryForFindDormitoryApplicationSettingResList(dormitoryForFindDormitoryApplicationSettingResList)
                .mealTicketResList(mealTicketResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

//    // Description : 입사 신청 설정 수정 - 변경 사항만 req 받아서 변경 ?
//    public ResponseEntity<?> modifyDormitoryApplicationSetting(CustomUserDetails customUserDetails, CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq, Long dormitoryApplicationSettingId) {
//
//        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
//        User user = userService.validateUserById(customUserDetails.getId());
//        School school = user.getSchool();
//
//        // ------------------------------------------
//        // TODO : JsonNullable을 이용하여 HTTP PATCH 구현
//
//
//
//    }

    // Description : 이전 작성 목록 조회
    public ResponseEntity<?> findDormitoryApplicationSettingHistory(CustomUserDetails customUserDetails, Integer page) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<DormitoryApplicationSetting> dormitoryApplicationSettingPage = dormitoryApplicationSettingRepository.findBySchool(pageRequest, school);

        // 비어 있을 시, 빈 리스트 or 예외 던질지 고민
        DefaultAssert.isTrue(!dormitoryApplicationSettingPage.isEmpty(), "입사 신청 설정 내역이 존재하지 않습니다.");

        List<DormitoryApplicationSetting> dormitoryApplicationSettingList = dormitoryApplicationSettingPage.getContent();
        List<FindDormitoryApplicationSettingHistoryRes> findDormitoryApplicationSettingHistoryResList = new ArrayList<>();

        for (DormitoryApplicationSetting dormitoryApplicationSetting : dormitoryApplicationSettingList) {
            FindDormitoryApplicationSettingHistoryRes findDormitoryApplicationSettingHistoryRes = FindDormitoryApplicationSettingHistoryRes.builder()
                    .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                    .title(dormitoryApplicationSetting.getTitle())
                    .startDate(dormitoryApplicationSetting.getStartDate())
                    .endDate(dormitoryApplicationSetting.getEndDate())
                    .build();
            findDormitoryApplicationSettingHistoryResList.add(findDormitoryApplicationSettingHistoryRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingHistoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public DormitoryApplicationSetting validateDormitoryApplicationSettingById(Long dormitoryApplicationSettingId) {

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "잘못된 입사 신청 설정 정보입니다.");
        return findDormitoryApplicationSetting.get();
    }

}
