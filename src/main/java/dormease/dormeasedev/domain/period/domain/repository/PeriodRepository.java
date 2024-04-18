package dormease.dormeasedev.domain.period.domain.repository;

import dormease.dormeasedev.domain.period.domain.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
}
