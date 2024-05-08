package dormease.dormeasedev.domain.refund_requestment.domain.respository;

import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.resident.domain.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRequestmentRepository extends JpaRepository<RefundRequestment, Long> {

    Optional<RefundRequestment> findByResident(Resident resident);

}
