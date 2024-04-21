package dormease.dormeasedev.domain.dormitory_setting_term.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitorySettingTermRepository extends JpaRepository<DormitorySettingTerm, Long> {
    List<DormitorySettingTerm> findByDormitory(Dormitory dormitory);

    List<DormitorySettingTerm> findByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);

}
