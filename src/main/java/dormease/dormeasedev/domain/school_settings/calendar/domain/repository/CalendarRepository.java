package dormease.dormeasedev.domain.school_settings.calendar.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c WHERE c.school = :school AND YEAR(c.startDate) = :year AND MONTH(c.startDate) = :month ORDER BY c.startDate")
    List<Calendar> findBySchoolAndYearAndMonth(@Param("school")School school, @Param("year") int year, @Param("month") int month);

    @Query("SELECT c FROM Calendar c WHERE c.school = :school AND c.startDate <= :date AND c.endDate >= :date")
    List<Calendar> findCalendarsByDate(@Param("school") School school, @Param("date") LocalDate date);
}
