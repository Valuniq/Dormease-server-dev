package dormease.dormeasedev.domain.school_settings.standard_setting.domain.repository;

import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardSettingRepository extends JpaRepository<StandardSetting, Long> {
}
