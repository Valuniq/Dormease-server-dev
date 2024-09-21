package dormease.dormeasedev.domain.school_settings.distance_score.domain.repository;

import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceScoreRepository extends JpaRepository<DistanceScore, Long> {
}
