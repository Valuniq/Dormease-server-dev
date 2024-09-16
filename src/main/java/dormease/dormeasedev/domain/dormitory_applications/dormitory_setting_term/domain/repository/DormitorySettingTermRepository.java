package dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.DormitorySettingTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitorySettingTermRepository extends JpaRepository<DormitorySettingTerm, Long> {

    List<DormitorySettingTerm> findByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);

//    List<DormitorySettingTerm> findByDormitoryAndDormitoryApplicationSetting_ApplicationStatus(Dormitory dormitory, ApplicationStatus applicationStatus);

//    boolean existsByDormitory(Dormitory dormitory);
}
