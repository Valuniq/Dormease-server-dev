package dormease.dormeasedev.domain.school_settings.standard_setting.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardSettingRepository extends JpaRepository<StandardSetting, Long> {

    boolean existsBySchool(School school);

    Optional<StandardSetting> findBySchool(School school);
}
