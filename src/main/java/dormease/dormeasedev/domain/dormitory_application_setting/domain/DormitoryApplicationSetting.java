package dormease.dormeasedev.domain.dormitory_application_setting.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.period.domain.Period;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 입사 신청 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryApplicationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_application_setting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 거주 기간 별 입/퇴사 날짜
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private Period period;

    private String title;

    // 시작 -> 마감일 中 시작일
    private LocalDate startDate;

    // 시작 -> 마감일 中 마감일
    private LocalDate endDate;

    // 보증금
    private Integer securityDeposit;

    // 이전/현재 어떤 상태인지 - 입사 신청 설정 내역과 구분을 위함
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Builder
    public DormitoryApplicationSetting(Long id, School school, Period period, String title, LocalDate startDate, LocalDate endDate, Integer securityDeposit, ApplicationStatus applicationStatus) {
        this.id = id;
        this.school = school;
        this.period = period;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.securityDeposit = securityDeposit;
        this.applicationStatus = applicationStatus;
    }
}
