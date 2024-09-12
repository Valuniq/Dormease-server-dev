package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.service.DormitoryService;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationDetailRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationSimpleRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.service.DormitoryTermService;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.service.MealTicketService;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.dormitory_applications.term.service.TermService;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
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
public class DormitoryApplicationService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final MealTicketRepository mealTicketRepository;
    private final DormitoryApplicationSettingRepository dormitoryapplicationsettingRepository;
    private final DormitoryTermRepository dormitoryTermRepository;

    private final UserService userService;
    private final DormitoryService dormitoryService;
    private final TermService termService;
    private final MealTicketService mealTicketService;
    private final DormitoryTermService dormitoryTermService;

    // Description : 입사 신청
    @Transactional
    public ResponseEntity<?> dormitoryApplication(CustomUserDetails customUserDetails, DormitoryApplicationReq dormitoryApplicationReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        Term term = termService.validateTermId(dormitoryApplicationReq.getTermId());
        Dormitory dormitory = dormitoryService.validateDormitoryId(dormitoryApplicationReq.getDormitoryId());
        MealTicket mealTicket = mealTicketService.validateMealTicketById(dormitoryApplicationReq.getMealTicketId());
        DormitoryApplicationSetting dormitoryApplicationSetting = term.getDormitoryApplicationSetting();
        DormitoryTerm dormitoryTerm = dormitoryTermService.validateDormitoryTermByTermAndDormitory(term, dormitory);

        int totalPrice = 0;
        totalPrice += mealTicket.getPrice(); // + 식권
        totalPrice += dormitoryTerm.getPrice(); // + 거주기간 별 기숙사 금액
        totalPrice += dormitoryApplicationSetting.getSecurityDeposit(); // + 입사신청설정에서 설정한 보증금

        DormitoryApplication dormitoryApplication = DormitoryApplication.builder()
                .user(user)
                .term(term)
                .dormitoryApplicationSetting(dormitoryApplicationSetting)
                .dormitory(dormitory)
                .mealTicket(mealTicket)
                .copy(dormitoryApplicationReq.getCopy())
                .prioritySelectionCopy(dormitoryApplicationReq.getPrioritySelectionCopy())
                .isSmoking(dormitoryApplicationReq.getIsSmoking())
                .emergencyContact(dormitoryApplicationReq.getEmergencyContact())
                .emergencyRelation(dormitoryApplicationReq.getEmergencyRelation())
                .bankName(dormitoryApplicationReq.getBankName())
                .accountNumber(dormitoryApplicationReq.getAccountNumber())
                .dormitoryPayment(false)
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
        Term term = dormitoryApplication.getTerm();
        // 기숙사
        Dormitory dormitory = dormitoryApplication.getDormitory();
        // 입사 신청 설정
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplication.getDormitoryApplicationSetting();

        DormitoryTerm dormitoryTerm = dormitoryTermService.validateDormitoryTermByTermAndDormitory(term, dormitory);

        MealTicket mealTicket = dormitoryApplication.getMealTicket();
        Integer mealTicketPrice = mealTicket.getPrice();

        // 총액 = 보증금 + 기숙사비 + 식권
        Integer totalPrice = dormitoryApplication.getTotalPrice();

        DormitoryApplicationDetailRes dormitoryApplicationDetailRes = DormitoryApplicationDetailRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .dormitoryApplicationSettingTitle(dormitoryApplicationSetting.getTitle())
                .schoolName(user.getName())
                .dormitoryName(dormitory.getName())
                //.gender(dormitory.getGender())// TODO: 수정 필요
                //.roomSize(dormitory.getRoomSize())    // TODO: 수정 필요
                .termName(term.getTermName())
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
            Term term = dormitoryApplication.getTerm();
            // 기숙사
            Dormitory dormitory = dormitoryApplication.getDormitory();
            // 입사 신청 설정
//            DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplication.getDormitoryApplicationSetting();
            Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryapplicationsettingRepository.findBySchoolAndDateRange(school, dormitoryApplication.getCreatedDate().toLocalDate());
            DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "입사 신청 날짜에 알맞은 입사 신청 설정이 존재하지 않습니다.");
            DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

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

    // Description : 입사 신청 상세 조회
    public ResponseEntity<?> findDormitoryApplication(CustomUserDetails customUserDetails, Long dormitoryApplicationId) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findById(dormitoryApplicationId);
        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "해당 id의 입사 신청이 존재하지 않습니다.");
        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();
        DefaultAssert.isTrue(dormitoryApplication.getUser().equals(user), "본인의 입사 신청만 조회할 수 있습니다.");

        // 거주 기간
        Term term = dormitoryApplication.getTerm();
        // 기숙사
        Dormitory dormitory = dormitoryApplication.getDormitory();
        // 입사 신청 설정
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplication.getDormitoryApplicationSetting();
        DormitoryTerm dormitoryTerm = dormitoryTermService.validateDormitoryTermByTermAndDormitory(term, dormitory);

        MealTicket mealTicket = dormitoryApplication.getMealTicket();
        Integer mealTicketPrice = mealTicket.getPrice();

        // 총액 = 보증금 + 기숙사비 + 식권
        Integer totalPrice = dormitoryApplication.getTotalPrice();

        DormitoryApplicationDetailRes dormitoryApplicationDetailRes = DormitoryApplicationDetailRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .dormitoryApplicationSettingTitle(dormitoryApplicationSetting.getTitle())
                .schoolName(user.getName())
                .dormitoryName(dormitory.getName())
                //.gender(dormitory.getGender())// TODO: 수정 필요
                //.roomSize(dormitory.getRoomSize())    // TODO: 수정 필요
                .termName(term.getTermName())
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


    // Description : 유효성 검증 함수
    public DormitoryApplication validateDormitoryApplicationByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus) {
        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatus(user, applicationStatus);
        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "해당 회원의 현재 입사 신청이 존재하지 않습니다.");
        return findDormitoryApplication.get();
    }

}
