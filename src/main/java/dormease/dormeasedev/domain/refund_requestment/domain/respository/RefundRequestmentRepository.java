package dormease.dormeasedev.domain.refund_requestment.domain.respository;

import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRequestmentRepository extends JpaRepository<RefundRequestment, Long> {

    Optional<RefundRequestment> findByResident(Resident resident);

    List<RefundRequestment> findAllBySchool(School school);
}
