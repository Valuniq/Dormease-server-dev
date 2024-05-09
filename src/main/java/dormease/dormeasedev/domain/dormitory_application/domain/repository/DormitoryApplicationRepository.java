package dormease.dormeasedev.domain.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {

    List<DormitoryApplication> findByUser(User user);

    Optional<DormitoryApplication> findByUserAndApplicationStatus(User user, ApplicationStatus applicationStatus);

    List<DormitoryApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

    List<DormitoryApplication> findByDormitoryTerm(DormitoryTerm dormitoryTerm);
}
