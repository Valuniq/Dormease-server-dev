package dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository;

import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionDistanceScoreRepository extends JpaRepository<RegionDistanceScore, Long> {
}
