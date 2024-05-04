package dormease.dormeasedev.domain.period.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private LocalDate startDate;
    
    private LocalDate endDate;

    // 입사, 퇴사, 환불, 룸메이트, 입금
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Builder
    public Period(Long id, LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
    }
}
