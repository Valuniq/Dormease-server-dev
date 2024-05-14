package dormease.dormeasedev.domain.requestment.domain.repository;

import dormease.dormeasedev.domain.requestment.domain.Requestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestmentRepository extends JpaRepository<Requestment, Long> {
}
