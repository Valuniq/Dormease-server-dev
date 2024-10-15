package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.users.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {

    Optional<DormitoryApplication> findByStudentAndApplicationStatus(Student student, ApplicationStatus applicationStatus);

    boolean existsByStudentAndApplicationStatus(Student student, ApplicationStatus applicationStatus);

    List<DormitoryApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

    DormitoryApplication findByStudentAndApplicationStatusAndDormitoryApplicationResult(Student student, ApplicationStatus applicationStatus, DormitoryApplicationResult dormitoryApplicationResult);

    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndStudent_StudentNumberContainingOrStudent_User_NameContaining(DormitoryApplicationSetting dormitoryApplicationSetting, String searchWord, String searchWord1);

    List<DormitoryApplication> findAllByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);

    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndDepositPaid(DormitoryApplicationSetting dormitoryApplicationSetting, boolean depositPaid);

    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndDepositPaidAndStudent_StudentNumberContainingOrStudent_User_NameContaining(DormitoryApplicationSetting dormitoryApplicationSetting, boolean depositPaid, String searchWord, String searchWord1);

    // 합격자 명단 - 입사 신청 설정과 기숙사로 입사 신청 조회
    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndDepositPaidAndResultDormitoryTerm_DormitoryRoomType_Dormitory_id(DormitoryApplicationSetting dormitoryApplicationSetting, boolean depositPaid, Long dormitoryId);

    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndDepositPaidAndResultDormitoryTerm_DormitoryRoomType_Dormitory_idAndStudent_StudentNumberContainingOrStudent_User_NameContaining(DormitoryApplicationSetting dormitoryApplicationSetting, boolean depositPaid, Long dormitoryId, String searchWord, String searchWord1);

    List<DormitoryApplication> findAllByDormitoryApplicationSettingAndDormitoryApplicationResultIn(
            DormitoryApplicationSetting dormitoryApplicationSetting,
            List<DormitoryApplicationResult> results
    );

    @Query("SELECT d FROM DormitoryApplication d " +
            "WHERE d.student = :student AND d.dormitoryApplicationResult = :dormitoryApplicationResult " +
            "ORDER BY d.createdDate DESC")
    DormitoryApplication findLatestDormitoryApplicationByStudent(Student student, DormitoryApplicationResult dormitoryApplicationResult);
}
