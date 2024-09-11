package dormease.dormeasedev.domain.points.point.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 상점 or 벌점
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    private Integer score;

    // 상,벌점 사유
    private String content;

    @Builder
    public Point(Long id, School school, PointType pointType, Integer score, String content) {
        this.id = id;
        this.school = school;
        this.pointType = pointType;
        this.score = score;
        this.content = content;
    }
}
