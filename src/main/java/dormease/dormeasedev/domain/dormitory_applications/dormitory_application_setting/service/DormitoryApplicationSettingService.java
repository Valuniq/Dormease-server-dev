package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryRoomTypeReq;
import dormease.dormeasedev.domain.dormitories.dormitory.service.DormitoryService;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryApplicationSettingSimpleRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryRoomTypeRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingHistoryRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto.DormitorySettingTermRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.DormitoryTermReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.TermRes;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.request.MealTicketReq;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.response.MealTicketRes;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.repository.TermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.request.TermReq;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.response.DormitoryTermRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
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
public class DormitoryApplicationSettingService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final TermRepository termRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final MealTicketRepository mealTicketRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;
    private final DormitoryRepository dormitoryRepository;

    private final UserService userService;
    private final DormitoryService dormitoryService;

    // Description : 입사 신청 설정 생성
    //  - 입사 신청 설정으로 저장 필요
    //      - 그 과정에서 Req 참고하여 알맞은 Entity에 저장하는 과정 필요
    @Transactional
    public ResponseEntity<?> createDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
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

        // DormitoryRoomTypeReq - 기숙사 자체 설정 관련
        for (DormitoryRoomTypeReq dormitoryRoomTypeReq : createDormitoryApplicationSettingReq.getDormitoryRoomTypeReqList()) {
            Optional<DormitoryRoomType> findDormitoryRoomType = dormitoryRoomTypeRepository.findById(dormitoryRoomTypeReq.getDormitoryRoomTypeId());
            DefaultAssert.isTrue(findDormitoryRoomType.isPresent(), "올바르지 않은 DormitoryRoomTypeId 입니다.");
            DormitoryRoomType dormitoryRoomType = findDormitoryRoomType.get();

            // DormitorySettingTerm save - 기숙사_방타입 : 입사 신청 설정 M:N 관계 중간 테이블
            DormitorySettingTerm dormitorySettingTerm = DormitorySettingTerm.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .dormitoryRoomType(dormitoryRoomType)
                    .acceptLimit(dormitoryRoomTypeReq.getAcceptLimit())
                    .build();
            dormitorySettingTermRepository.save(dormitorySettingTerm);
        }

        for (TermReq termReq : createDormitoryApplicationSettingReq.getTermReqList()) {
            Term term = Term.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .termName(termReq.getTermName())
                    .startDate(termReq.getStartDate())
                    .endDate(termReq.getEndDate())
                    .build();
            termRepository.save(term);

            for (DormitoryTermReq dormitoryTermReq : termReq.getDormitoryTermReqList()) {
                Optional<DormitoryRoomType> findDormitoryRoomType = dormitoryRoomTypeRepository.findById(dormitoryTermReq.getDormitoryRoomTypeId());
                DefaultAssert.isTrue(findDormitoryRoomType.isPresent(), "올바르지 않은 DormitoryRoomTypeId 입니다.");
                DormitoryRoomType dormitoryRoomType = findDormitoryRoomType.get();

                // 아래 if문 예외 처리는 ui 변경하여 입사신청 안받을 경우 0명 기입 대신 아예 칸을 없애는 방향으로 논의!
//                if (dormitoryTermReq.getPrice() == 0)
//                    continue;

                DormitoryTerm dormitoryTerm = DormitoryTerm.builder()
                        .term(term)
                        .dormitoryRoomType(dormitoryRoomType)
                        .price(dormitoryTermReq.getPrice())
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
    public ResponseEntity<?> findDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId) {

        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        DefaultAssert.isTrue(dormitoryApplicationSetting.getSchool().equals(school), "다른 학교의 입사 신청 설정을 조회할 수 없습니다.");

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

        // 기숙사_방타입 관련
        List<DormitorySettingTerm> findDormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitorySettingTermRes> dormitorySettingTermResList = new ArrayList<>();
        for (DormitorySettingTerm dormitorySettingTerm : findDormitorySettingTermList) {
            DormitoryRoomType dormitoryRoomType = dormitorySettingTerm.getDormitoryRoomType();
            Dormitory dormitory = dormitoryRoomType.getDormitory();
            RoomType roomType = dormitoryRoomType.getRoomType();

            DormitorySettingTermRes dormitorySettingTermRes = DormitorySettingTermRes.builder()
                    .dormitoryRoomTypeId(dormitoryRoomType.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(roomType.getRoomSize())
                    .gender(roomType.getGender())
                    .acceptLimit(dormitorySettingTerm.getAcceptLimit())
                    .build();
            dormitorySettingTermResList.add(dormitorySettingTermRes);
        }

        // 거주기간 관련
        List<Term> termList = termRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<TermRes> termResList = new ArrayList<>();
        for (Term term : termList) {
            List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
            List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();
            for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
                DormitoryTermRes dormitoryTermRes = DormitoryTermRes.builder()
                        .dormitoryTermId(dormitoryTerm.getId())
                        .dormitoryRoomTypeId(dormitoryTerm.getDormitoryRoomType().getId())
                        .price(dormitoryTerm.getPrice())
                        .build();
                dormitoryTermResList.add(dormitoryTermRes);
            }

            TermRes termRes = TermRes.builder()
                    .termId(term.getId())
                    .termName(term.getTermName())
                    .startDate(term.getStartDate())
                    .endDate(term.getEndDate())
                    .dormitoryTermResList(dormitoryTermResList)
                    .build();
            termResList.add(termRes);
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
                .dormitorySettingTermResList(dormitorySettingTermResList)
                .termResList(termResList)
                .mealTicketResList(mealTicketResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

//    // Description : 입사 신청 설정 수정 - 변경 사항만 req 받아서 변경 ?
//    public ResponseEntity<?> modifyDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq, Long dormitoryApplicationSettingId) {
//
//        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
//        User user = userService.validateUserById(userDetailsImpl.getUserId());
//        School school = user.getSchool();
//
//        // ------------------------------------------
//        // TODO : JsonNullable을 이용하여 HTTP PATCH 구현
//
//
//
//    }

    // Description : 이전 작성 목록 조회
    public ResponseEntity<?> findDormitoryApplicationSettingHistory(UserDetailsImpl userDetailsImpl) {

        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        List<DormitoryApplicationSetting> dormitoryApplicationSettingList = dormitoryApplicationSettingRepository.findTop3BySchoolAndApplicationStatusOrderByCreatedDateDesc(school, ApplicationStatus.BEFORE);

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

    // Description : 입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회
    public ResponseEntity<?> findDormitories(UserDetailsImpl userDetailsImpl) {

        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        List<Dormitory> findDormitoryList = dormitoryRepository.findBySchool(school);
        List<DormitoryRoomTypeRes> dormitoryRoomTypeResList = new ArrayList<>();
        for (Dormitory dormitory : findDormitoryList) {

            List<DormitoryRoomType> findDormitoryRoomTypeList = dormitoryRoomTypeRepository.findByDormitory(dormitory);
            for (DormitoryRoomType dormitoryRoomType : findDormitoryRoomTypeList) {
                RoomType roomType = dormitoryRoomType.getRoomType();
                DormitoryRoomTypeRes dormitoryRoomTypeRes = DormitoryRoomTypeRes.builder()
                        .dormitoryRoomTypeId(dormitoryRoomType.getId())
                        .dormitoryName(dormitory.getName())
                        .roomSize(roomType.getRoomSize())
                        .gender(roomType.getGender())
                        .build();
                dormitoryRoomTypeResList.add(dormitoryRoomTypeRes);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryRoomTypeResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 이전 입사 신청 설정 목록 (신청자 명단 프로세스)
    public ResponseEntity<?> findDormitoryApplicationSettings(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        List<DormitoryApplicationSetting> dormitoryApplicationSettingList = dormitoryApplicationSettingRepository.findAllBySchoolAndApplicationStatus(school, ApplicationStatus.BEFORE);
        List<DormitoryApplicationSettingSimpleRes> dormitoryApplicationSettingSimpleResList = new ArrayList<>();
        for (DormitoryApplicationSetting dormitoryApplicationSetting : dormitoryApplicationSettingList) {
            DormitoryApplicationSettingSimpleRes dormitoryApplicationSettingSimpleRes = DormitoryApplicationSettingSimpleRes.builder()
                    .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                    .title(dormitoryApplicationSetting.getTitle())
                    .createdDate(dormitoryApplicationSetting.getCreatedDate().toLocalDate())
                    .build();
            dormitoryApplicationSettingSimpleResList.add(dormitoryApplicationSettingSimpleRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationSettingSimpleResList)
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
