package dormease.dormeasedev.domain.period.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Period extends BaseEntity {

    // TODO : 시작 날짜, 마감 날짜 - 시간 관련 생각
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime startDate;
    
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Builder
    public Period(Long id, School school, LocalDateTime startDate, LocalDateTime endDate, PeriodType periodType) {
        this.id = id;
        this.school = school;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
    }
}
