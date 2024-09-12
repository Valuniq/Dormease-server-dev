package dormease.dormeasedev.domain.dormitory_applications.meal_ticket.service;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.response.MealTicketRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
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
public class MealTicketService {

    private final MealTicketRepository mealTicketRepository;
    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;

    private final UserService userService;

    // Description : 식권 목록 조회 (for 입사 신청(app))
    public ResponseEntity<?> findMealTicketList(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "현재 입사 신청 설정이 존재하지 않습니다.");
        DormitoryApplicationSetting dormitoryApplicationSetting = findDormitoryApplicationSetting.get();

        List<MealTicket> mealTicketList = mealTicketRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<MealTicketRes> mealTicketResList = new ArrayList<>();
        for (MealTicket mealTicket : mealTicketList) {
            MealTicketRes mealTicketRes = MealTicketRes.builder()
                    .id(mealTicket.getId())
                    .count(mealTicket.getCount())
                    .price(mealTicket.getPrice())
                    .build();
            mealTicketResList.add(mealTicketRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(mealTicketResList)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    // Description : 유효성 검증 함수
    public MealTicket validateMealTicketById(Long mealTicketId) {
        Optional<MealTicket> findMealTicket = mealTicketRepository.findById(mealTicketId);
        DefaultAssert.isTrue(findMealTicket.isPresent(), "잘못된 식권 정보입니다.");
        return findMealTicket.get();
    }
}
