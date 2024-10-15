package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryApplicationSettingRepository extends JpaRepository<DormitoryApplicationSetting, Long> {

    List<DormitoryApplicationSetting> findTop3BySchoolAndApplicationStatusOrderByCreatedDateDesc(School school, ApplicationStatus applicationStatus);

    // 가장 최근 입사 신청 설정을 찾는 쿼리
    Optional<DormitoryApplicationSetting> findTopBySchoolAndApplicationStatusOrderByStartDateDesc(School school, ApplicationStatus applicationStatus);
    // 진행 중 입사 신청 설정도 볼 수 있음
    Optional<DormitoryApplicationSetting> findTopBySchoolOrderByStartDateDesc(School school);
    
    // ApplicationStatus.NOW 전용!! / BEFORE 하면 여러 개 나오기에 안됨
    Optional<DormitoryApplicationSetting> findBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);

    Optional<DormitoryApplicationSetting> findBySchoolAndApplicationStatusNot(School school, ApplicationStatus applicationStatus);;

    List<DormitoryApplicationSetting> findAllBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);

    List<DormitoryApplicationSetting> findAllBySchoolAndApplicationStatusNotIn(School school, List<ApplicationStatus> applicationStatuses);

    Optional<DormitoryApplicationSetting> findAllBySchoolAndApplicationStatusIn(School school, List<ApplicationStatus> applicationStatuses);


    // 날짜로 입사신청설정 찾기 : 입사 신청 날짜로 입사 신청 설정 찾기 가능
//    Optional<DormitoryApplicationSetting> findBySchoolAndStartDateLessThanEqualAndEndDateGreaterThanEqual(School school, LocalDate createdDate, LocalDate createdDate2);
    @Query("SELECT d FROM DormitoryApplicationSetting d WHERE d.school = :school AND d.startDate <= :createdDate AND d.endDate >= :createdDate")
    Optional<DormitoryApplicationSetting> findBySchoolAndDateRange(@Param("school") School school, @Param("createdDate") LocalDate createdDate);

    boolean existsBySchoolAndApplicationStatus(School school, ApplicationStatus applicationStatus);

    List<DormitoryApplicationSetting> findBySchoolOrderByCreatedDateDesc(School school);
}
