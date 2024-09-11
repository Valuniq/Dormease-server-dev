package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DormitoryApplicationSettingRepository extends JpaRepository<DormitoryApplicationSetting, Long> {

    Page<DormitoryApplicationSetting> findBySchoolAndApplicationStatus(Pageable pageable, School school, ApplicationStatus applicationStatus);

    // ApplicationStatus.NOW 전용!! / BEFORE 하면 여러 개 나오기에 안됨
    Optional<DormitoryApplicationSetting> findBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);

    // 날짜로 입사신청설정 찾기 : 입사 신청 날짜로 입사 신청 설정 찾기 가능
//    Optional<DormitoryApplicationSetting> findBySchoolAndStartDateLessThanEqualAndEndDateGreaterThanEqual(School school, LocalDate createdDate, LocalDate createdDate2);
    @Query("SELECT d FROM DormitoryApplicationSetting d WHERE d.school = :school AND d.startDate <= :createdDate AND d.endDate >= :createdDate")
    Optional<DormitoryApplicationSetting> findBySchoolAndDateRange(@Param("school") School school, @Param("createdDate") LocalDate createdDate);

    boolean existsBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);


}
