package dormease.dormeasedev.domain.dormitory_application.domain.repository;

import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DormitoryApplicationRepository extends JpaRepository<DormitoryApplication, Long> {
}
