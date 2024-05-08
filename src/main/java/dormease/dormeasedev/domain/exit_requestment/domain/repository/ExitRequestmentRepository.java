package dormease.dormeasedev.domain.exit_requestment.domain.repository;

import dormease.dormeasedev.domain.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExitRequestmentRepository extends JpaRepository<ExitRequestment, Long> {

    List<ExitRequestment> findAllBySchool(School school);
}
