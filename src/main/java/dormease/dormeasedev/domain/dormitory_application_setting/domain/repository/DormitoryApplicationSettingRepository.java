package dormease.dormeasedev.domain.dormitory_application_setting.domain.repository;

import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DormitoryApplicationSettingRepository extends JpaRepository<DormitoryApplicationSetting, Long> {

    Page<DormitoryApplicationSetting> findBySchool(Pageable pageable, School school);

    DormitoryApplicationSetting findBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);
}
