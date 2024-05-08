package dormease.dormeasedev.domain.refund_requestment.domain.respository;

import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRequestmentRepository extends JpaRepository<RefundRequestment, Long> {
}
