package dormease.dormeasedev.domain.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.term.domain.Term;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {

    List<DormitoryApplication> findByUser(User user);

    Optional<DormitoryApplication> findByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus);

    boolean existsByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus);

    List<DormitoryApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

    List<DormitoryApplication> findByTerm(Term term);

    @Query("SELECT da FROM DormitoryApplication da WHERE da.user = :user AND da.dormitoryApplicationResult = :results ORDER BY da.createdDate DESC")
    Optional<DormitoryApplication> findTop1ByUserAndResultsOrderByCreatedDateDesc(User user, DormitoryApplicationResult results);

    Optional<DormitoryApplication> findByDormitoryAndApplicationStatus(Dormitory sameNameAndSameGenderDormitory, ApplicationStatus applicationStatus);

    DormitoryApplication findByUserAndApplicationStatusAndDormitoryApplicationResult(User user, ApplicationStatus applicationStatus, DormitoryApplicationResult dormitoryApplicationResult);

    Page<DormitoryApplication> findDormitoryApplicaionsByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting, Pageable pageable);

    List<DormitoryApplication> findAllByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);
}
