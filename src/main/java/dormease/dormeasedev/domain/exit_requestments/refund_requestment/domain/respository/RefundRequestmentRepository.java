package dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.respository;

import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRequestmentRepository extends JpaRepository<RefundRequestment, Long> {

    Optional<RefundRequestment> findByResident(Resident resident);

    Page<RefundRequestment> findRefundRequestmentsByResident_User_School(School school, Pageable pageable);

    boolean existsByResident(Resident resident);
}
