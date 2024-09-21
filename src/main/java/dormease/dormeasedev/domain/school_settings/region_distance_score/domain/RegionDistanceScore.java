package dormease.dormeasedev.domain.school_settings.region_distance_score.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RegionDistanceScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_distance_score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distance_score_id")
    private DistanceScore distanceScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Builder
    public RegionDistanceScore(School school, DistanceScore distanceScore, Region region) {
        this.school = school;
        this.distanceScore = distanceScore;
        this.region = region;
    }
}
