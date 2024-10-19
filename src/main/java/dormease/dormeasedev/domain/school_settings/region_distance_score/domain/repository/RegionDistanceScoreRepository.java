package dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionDistanceScoreRepository extends JpaRepository<RegionDistanceScore, Long> {

    List<RegionDistanceScore> findBySchoolAndDistanceScore(School school, DistanceScore distanceScore);

    List<RegionDistanceScore> findBySchool(School school);

    @Query("SELECT rds FROM RegionDistanceScore rds JOIN FETCH rds.distanceScore " +
            "WHERE rds.school = :school AND rds.region = :region")
    Optional<RegionDistanceScore> findBySchoolAndRegionWithDistanceScore(
            @Param("school") School school,
            @Param("region") Region region
    );

}
