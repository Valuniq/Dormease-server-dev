package dormease.dormeasedev.domain.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {

    Optional<DormitoryApplication> findByUser(User user);
}
