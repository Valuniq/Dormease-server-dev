package dormease.dormeasedev.domain.school_settings.setting.domain;

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
public class Setting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settings_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 흡연 설정 활성화 여부
    private Boolean isSmokedOn;
    // 우선 선발 설정 활성화 여부
    private Boolean isPrioritySelectionOn;
    // 이동 합격 설정 활성화 여부
    private Boolean isMoveSelectionOn;
    // 동일 기간 설정 활성화 여부
    private Boolean isSameTermOn;
    // 상/벌점 활성화 여부
    private Boolean isPointOn;

    // 개인 정보 활용 동의서
    @Lob
    private String agreementContent;

    @Builder
    public Setting(Long id, School school, Boolean isSmokedOn, Boolean isPrioritySelectionOn, Boolean isMoveSelectionOn, Boolean isSameTermOn, Boolean isPointOn, String agreementContent) {
        this.id = id;
        this.school = school;
        this.isSmokedOn = isSmokedOn;
        this.isPrioritySelectionOn = isPrioritySelectionOn;
        this.isMoveSelectionOn = isMoveSelectionOn;
        this.isSameTermOn = isSameTermOn;
        this.isPointOn = isPointOn;
        this.agreementContent = agreementContent;
    }
}
