package dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.MealTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealTicketRepository extends JpaRepository<MealTicket, Long> {

    List<MealTicket> findByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);

    Optional<MealTicket> findByDormitoryApplicationSettingAndPrice(DormitoryApplicationSetting dormitoryApplicationSetting, Integer price);
}
