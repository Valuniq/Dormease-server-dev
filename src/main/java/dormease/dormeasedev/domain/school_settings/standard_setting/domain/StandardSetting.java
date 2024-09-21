package dormease.dormeasedev.domain.school_settings.standard_setting.domain;

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
public class StandardSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stancard_settings_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 최소 학점
    private double minScore;

    private int scoreRatio;

    private int distanceRatio;

    private boolean pointReflection;

    private TiePriority tiePriority;

    private FreshmanStandard freshmanStandard;

    private boolean prioritySelection;

    private boolean movePassSelection;

    private boolean sameSmoke;

    private boolean sameTerm;

    // 입사 서약서
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String entrancePledge;

    @Builder
    public StandardSetting(School school, double minScore, int scoreRatio, int distanceRatio, boolean pointReflection, TiePriority tiePriority, FreshmanStandard freshmanStandard, boolean prioritySelection, boolean movePassSelection, boolean sameSmoke, boolean sameTerm, String entrancePledge) {
        this.school = school;
        this.minScore = minScore;
        this.scoreRatio = scoreRatio;
        this.distanceRatio = distanceRatio;
        this.pointReflection = pointReflection;
        this.tiePriority = tiePriority;
        this.freshmanStandard = freshmanStandard;
        this.prioritySelection = prioritySelection;
        this.movePassSelection = movePassSelection;
        this.sameSmoke = sameSmoke;
        this.sameTerm = sameTerm;
        this.entrancePledge = entrancePledge;
    }
}
