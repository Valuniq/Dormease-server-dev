package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
