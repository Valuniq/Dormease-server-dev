package dormease.dormeasedev.domain.requestment.domain.repository;

import dormease.dormeasedev.domain.requestment.domain.Requestment;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestmentRepository extends JpaRepository<Requestment, Long> {

    Page<Requestment> findRequestmentsBySchool(School school, Pageable pageable);
}
