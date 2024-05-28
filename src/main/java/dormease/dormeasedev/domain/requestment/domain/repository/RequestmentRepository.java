package dormease.dormeasedev.domain.requestment.domain.repository;

import dormease.dormeasedev.domain.requestment.domain.Requestment;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestmentRepository extends JpaRepository<Requestment, Long> {

    Page<Requestment> findRequestmentsByResident_User_SchoolAndVisibility(School school, Boolean visibility, Pageable pageable);

    Optional<Requestment> findByIdAndResident_User_School(Long requestmentId, School school);

    Optional<Requestment> findByIdAndResident(Long requestmentId, Resident resident);

    Page<Requestment> findRequestmentsByResident(Resident resident, Pageable pageable);
}
