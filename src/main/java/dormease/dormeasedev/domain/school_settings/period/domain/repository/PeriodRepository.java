package dormease.dormeasedev.domain.school_settings.period.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.period.domain.Period;
import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    Optional<Period> findBySchoolAndPeriodType(School school, PeriodType periodType);
}
