package dormease.dormeasedev.domain.exit_requestment.domain.repository;

import dormease.dormeasedev.domain.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitRequestmentRepository extends JpaRepository<ExitRequestment, Long> {

    Page<ExitRequestment> findExitRequestmentsBySchool(School school, Pageable pageable);
}
