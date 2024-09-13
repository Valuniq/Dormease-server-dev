package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private String title;

    // 입사 신청
    private LocalDate startDate;

    private LocalDate endDate;

    // 입금
    private LocalDate depositStartDate;

    private LocalDate depositEndDate;

    // 보증금
    private Integer securityDeposit;

    // 이전/현재 어떤 상태인지 - 입사 신청 설정 내역과 구분을 위함
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Builder
    public DormitoryApplicationSetting(School school, String title, LocalDate startDate, LocalDate endDate, LocalDate depositStartDate, LocalDate depositEndDate, Integer securityDeposit) {
        this.school = school;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositStartDate = depositStartDate;
        this.depositEndDate = depositEndDate;
        this.securityDeposit = securityDeposit;
        this.applicationStatus = ApplicationStatus.READY;
    }
}
