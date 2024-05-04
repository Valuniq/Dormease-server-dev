package dormease.dormeasedev.domain.dormitory_application.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.service.DormitoryService;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_term.service.DormitoryTermService;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.meal_ticket.service.MealTicketService;
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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;

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

    // Description : 입사 신청 내역 조회
//    public ResponseEntity<?> findMyDormitoryApplication(CustomUserDetails customUserDetails) {
//
//        User user = userService.validateUserById(customUserDetails.getId());
//
//        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUser(user);
//        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "유저의 현재 입사 신청 내역이 존재하지 않습니다.");
//        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();
//
//
//    }
}
