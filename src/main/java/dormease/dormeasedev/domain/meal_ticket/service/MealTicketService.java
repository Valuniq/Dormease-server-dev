package dormease.dormeasedev.domain.meal_ticket.service;

import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.global.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MealTicketService {

    private final MealTicketRepository mealTicketRepository;


    // Description : 유효성 검증 함수
    public MealTicket validateMealTicketById(Long mealTicketId) {
        Optional<MealTicket> findMealTicket = mealTicketRepository.findById(mealTicketId);
        DefaultAssert.isTrue(findMealTicket.isPresent(), "잘못된 식권 정보입니다.");
        return findMealTicket.get();
    }
}
