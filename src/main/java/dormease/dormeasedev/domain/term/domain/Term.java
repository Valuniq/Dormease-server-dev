package dormease.dormeasedev.domain.term.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
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

    // 입사-퇴사 기간 여기 넣을지?

    // 이전 내역인지 구분하는 컬럼

    @Builder
    public Term(Long id, String termName, DormitoryApplicationSetting dormitoryApplicationSetting, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.dormitoryApplicationSetting = dormitoryApplicationSetting;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
