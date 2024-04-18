package dormease.dormeasedev.domain.dormitory_setting_term.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기숙사 - dormitoryTerm(입사 신청 설정  페이지에서 저장하는 학기)의 중간테이블
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class DormitorySettingTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_setting_term_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_term_id")
    private DormitoryTerm dormitoryTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_application_setting_id")
    private DormitoryApplicationSetting dormitoryApplicationSetting;

    @Builder
    public DormitorySettingTerm(Long id, Dormitory dormitory, DormitoryTerm dormitoryTerm, DormitoryApplicationSetting dormitoryApplicationSetting) {
        this.id = id;
        this.dormitory = dormitory;
        this.dormitoryTerm = dormitoryTerm;
        this.dormitoryApplicationSetting = dormitoryApplicationSetting;
    }
}
