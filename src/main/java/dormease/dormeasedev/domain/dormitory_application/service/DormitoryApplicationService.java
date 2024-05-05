package dormease.dormeasedev.domain.dormitory_application.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.service.DormitoryService;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.domain.dormitory_application.dto.response.DormitoryApplicationDetailRes;
import dormease.dormeasedev.domain.dormitory_application.dto.response.DormitoryApplicationSimpleRes;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.service.DormitoryTermService;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.meal_ticket.service.MealTicketService;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final MealTicketRepository mealTicketRepository;

    private final UserService userService;
    private final DormitoryService dormitoryService;
    private final DormitoryTermService dormitoryTermService;
    private final MealTicketService mealTicketService;

    // Description : 입사 신청
    @Transactional
    public ResponseEntity<?> dormitoryApplication(CustomUserDetails customUserDetails, DormitoryApplicationReq dormitoryApplicationReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        DormitoryTerm dormitoryTerm = dormitoryTermService.validateDormitoryTermId(dormitoryApplicationReq.getDormitoryTermId());
        Dormitory dormitory = dormitoryService.validateDormitoryId(dormitoryApplicationReq.getDormitoryId());
        MealTicket mealTicket = mealTicketService.validateMealTicketById(dormitoryApplicationReq.getMealTicketId());

//        List<DormitorySettingTerm> dormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryAndDormitoryApplicationSetting_ApplicationStatus(dormitory, ApplicationStatus.NOW);
//        DormitoryApplicationSetting dormitoryApplicationSetting = dormitorySettingTermList.get(0).getDormitoryApplicationSetting();

        Optional<DormitorySettingTerm> findDormitorySettingTerm = dormitorySettingTermRepository.findByDormitoryAndDormitoryApplicationSetting_ApplicationStatus(dormitory, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitorySettingTerm.isPresent(), "해당 기숙사에 대한 입사 신청 설정이 존재하지 않습니다.");
        DormitorySettingTerm dormitorySettingTerm = findDormitorySettingTerm.get();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitorySettingTerm.getDormitoryApplicationSetting();

        int totalPrice = 0;
        totalPrice += mealTicket.getPrice(); // + 식권
        totalPrice += dormitoryTerm.getPrice(); // + 거주기간 별 기숙사 금액
        totalPrice += dormitoryApplicationSetting.getSecurityDeposit(); // + 입사신청설정에서 설정한 보증금

        DormitoryApplication dormitoryApplication = DormitoryApplication.builder()
                .user(user)
                .dormitoryTerm(dormitoryTerm)
                .dormitoryApplicationSetting(dormitoryApplicationSetting)
                .copy(dormitoryApplicationReq.getCopy())
                .prioritySelectionCopy(dormitoryApplicationReq.getPrioritySelectionCopy())
                .isSmoking(dormitoryApplicationReq.getIsSmoking())
                .emergencyContact(dormitoryApplicationReq.getEmergencyContact())
                .emergencyRelation(dormitoryApplicationReq.getEmergencyRelation())
                .bankName(dormitoryApplicationReq.getBankName())
                .accountNumber(dormitoryApplicationReq.getAccountNumber())
                .dormitoryApplicationResult(DormitoryApplicationResult.WAIT)
                .totalPrice(totalPrice)
                .applicationStatus(ApplicationStatus.NOW)
                .build();

        dormitoryApplicationRepository.save(dormitoryApplication);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("입사 신청이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 내역 조회 ( 현재 )
    public ResponseEntity<?> findMyDormitoryApplication(CustomUserDetails customUserDetails) {

        // TODO : 필요한 컬럼
        // 입사 신청 설정 제목 (d a s), 학교명(school), 기숙사명(dormitory), 성별, 인실, (거주) 기간(d t), 식권(m t), 우선 선발 증빙 서류 여부(d a), 등본 여부, 흡연 여부
        // 보증금(d a s), 기숙사비 + 식권(d t, m t), 총액(보증금 + 기숙사비 + 식권)(a s t, d t, m t), 비상 연락처(d a), 비상 연락처와의 관계, 은행명, 계좌 번호, 신청 결과

        // TODO : 필요한 엔티티
        //  - ( 회원 ) / 입사 신청 설정 , 학교, 기숙사 , 거주 기간 , 식권, 입사 신청

        // 회원, 학교
        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // 입사 신청
        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatus(user, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "유저의 현재 입사 신청 내역이 존재하지 않습니다.");
        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();

        // 거주 기간
        DormitoryTerm dormitoryTerm = dormitoryApplication.getDormitoryTerm();

        // 기숙사
        Dormitory dormitory = dormitoryTerm.getDormitory();

        // 입사 신청 설정
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplication.getDormitoryApplicationSetting();

        // 총액 = 보증금 + 기숙사비 + 식권
        Integer totalPrice = dormitoryApplication.getTotalPrice();
        Integer mealTicketPrice = totalPrice - dormitoryTerm.getPrice() -dormitoryApplicationSetting.getSecurityDeposit();
        Optional<MealTicket> findMealTicket = mealTicketRepository.findByDormitoryApplicationSettingAndPrice(dormitoryApplicationSetting, mealTicketPrice);
        DefaultAssert.isTrue(findMealTicket.isPresent(), "식권 정보가 올바르지 않습니다.");
        MealTicket mealTicket = findMealTicket.get();

        DormitoryApplicationDetailRes dormitoryApplicationDetailRes = DormitoryApplicationDetailRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .dormitoryApplicationSettingTitle(dormitoryApplicationSetting.getTitle())
                .schoolName(user.getName())
                .dormitoryName(dormitory.getName())
                .gender(dormitory.getGender())
                .roomSize(dormitory.getRoomSize())
                .term(dormitoryTerm.getTerm())
                .mealTicketCount(mealTicket.getCount())
//                 null이면 제출 x
                .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy() != null)
                .copy(dormitoryApplication.getCopy() != null)
                .smoking(dormitoryApplication.getIsSmoking())
                .securityDeposit(dormitoryApplicationSetting.getSecurityDeposit())
                .dormitoryPlusMealTicketPrice(dormitoryTerm.getPrice() + mealTicketPrice)
                .totalPrice(totalPrice)
                .emergencyContact(dormitoryApplication.getEmergencyContact())
                .emergencyRelation(dormitoryApplication.getEmergencyRelation())
                .bankName(dormitoryApplication.getBankName())
                .accountNumber(dormitoryApplication.getAccountNumber())
                .dormitoryApplicationResult(dormitoryApplication.getDormitoryApplicationResult())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 이전 입사 신청 내역 목록 조회 (이번 입사 신청 내역 제외)
    public ResponseEntity<?> findMyDormitoryApplicationHistory(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        List<DormitoryApplicationSimpleRes> dormitoryApplicationSimpleResList = new ArrayList<>();
        // 이전 입사 신청 목록
        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllByApplicationStatus(ApplicationStatus.BEFORE);

        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            // 거주 기간
            DormitoryTerm dormitoryTerm = dormitoryApplication.getDormitoryTerm();
            // 기숙사
            Dormitory dormitory = dormitoryTerm.getDormitory();
            // 입사 신청 설정
            DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplication.getDormitoryApplicationSetting();

            DormitoryApplicationSimpleRes dormitoryApplicationSimpleRes = DormitoryApplicationSimpleRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .dormitoryApplicationSettingTitle(dormitoryApplicationSetting.getTitle())
                    .createdDate(dormitoryApplicationSetting.getCreatedDate().toLocalDate())
                    .build();
            dormitoryApplicationSimpleResList.add(dormitoryApplicationSimpleRes);

        }
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationSimpleResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 이동 합격 수락
    @Transactional
    public ResponseEntity<?> acceptMovePass(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatus(user, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "현재 입사 신청 내역이 존재하지 않습니다.");
        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();

        dormitoryApplication.updateDormitoryApplicationResult(DormitoryApplicationResult.PASS);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("이동 합격 수락이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 이동 합격 거절
    @Transactional
    public ResponseEntity<?> rejectMovePass(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatus(user, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "현재 입사 신청 내역이 존재하지 않습니다.");
        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();

        // '거절 시 불합격 처리'라고 피그마에 존재
        dormitoryApplication.updateDormitoryApplicationResult(DormitoryApplicationResult.NON_PASS);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("이동 합격 거절이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
