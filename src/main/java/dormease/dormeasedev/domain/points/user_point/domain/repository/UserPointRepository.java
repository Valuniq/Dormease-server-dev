package dormease.dormeasedev.domain.points.user_point.domain.repository;

import dormease.dormeasedev.domain.points.point.domain.Point;
import dormease.dormeasedev.domain.points.point.domain.PointType;
import dormease.dormeasedev.domain.points.user_point.domain.UserPoint;
import dormease.dormeasedev.domain.users.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
    List<UserPoint> findByUser(User user);

    Page<UserPoint> findUserPointsByUser(User user, Pageable pageable);

    List<UserPoint> findByPoint(Point point);

    List<UserPoint> findUserPointsByUserAndPoint_pointTypeOrderByCreatedDateDesc(User user, PointType pointType);
}
