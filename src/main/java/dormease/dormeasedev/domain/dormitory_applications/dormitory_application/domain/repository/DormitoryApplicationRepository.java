package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {

    Optional<DormitoryApplication> findByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus);

    boolean existsByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus);

    List<DormitoryApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

//    @Query("SELECT da FROM DormitoryApplication da WHERE da.user = :user AND da.dormitoryApplicationResult = :results ORDER BY da.createdDate DESC")
//    Optional<DormitoryApplication> findTop1ByUserAndResultsOrderByCreatedDateDesc(User user, DormitoryApplicationResult results);

//    Optional<DormitoryApplication> findByDormitoryAndApplicationStatus(Dormitory sameNameAndSameGenderDormitory, ApplicationStatus applicationStatus);

//    DormitoryApplication findByUserAndApplicationStatusAndDormitoryApplicationResult(User user, ApplicationStatus applicationStatus, DormitoryApplicationResult dormitoryApplicationResult);
    DormitoryApplication findByStudentAndApplicationStatusAndDormitoryApplicationResult(Student student, ApplicationStatus applicationStatus, DormitoryApplicationResult dormitoryApplicationResult);
}
