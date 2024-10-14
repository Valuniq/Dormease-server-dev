package dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionDistanceScoreRepository extends JpaRepository<RegionDistanceScore, Long> {

    List<RegionDistanceScore> findBySchoolAndDistanceScore(School school, DistanceScore distanceScore);

    List<RegionDistanceScore> findBySchool(School school);
}
