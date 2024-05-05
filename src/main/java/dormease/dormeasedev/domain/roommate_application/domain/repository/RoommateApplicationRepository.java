package dormease.dormeasedev.domain.roommate_application.domain.repository;

import dormease.dormeasedev.domain.roommate_application.domain.RoommateApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoommateApplicationRepository extends JpaRepository<RoommateApplication, Long> {
}
