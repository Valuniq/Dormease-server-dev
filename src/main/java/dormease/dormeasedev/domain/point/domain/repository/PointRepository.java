package dormease.dormeasedev.domain.point.domain.repository;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.point.domain.Point;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findBySchoolAndStatus(School school, Status status);
}
