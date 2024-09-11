package dormease.dormeasedev.domain.dormitory_applications.term.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 거주 기간
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_application_setting_id")
    private DormitoryApplicationSetting dormitoryApplicationSetting;

    // 거주 기간
    private String termName;

    // 시작 -> 마감일 中 시작일
    private LocalDate startDate;

    // 시작 -> 마감일 中 마감일
    private LocalDate endDate;

    // 이전 내역인지 구분하는 컬럼 : x => 기간에 따라 배치로 정리

    @Builder
    public Term(String termName, DormitoryApplicationSetting dormitoryApplicationSetting, LocalDate startDate, LocalDate endDate) {
        this.dormitoryApplicationSetting = dormitoryApplicationSetting;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
