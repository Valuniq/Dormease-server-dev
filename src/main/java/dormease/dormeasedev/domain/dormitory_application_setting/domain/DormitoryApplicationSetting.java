package dormease.dormeasedev.domain.dormitory_application_setting.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryApplicationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_application_setting_id")
    private Long id;

    private String title;

    // 보증금
    private String securityDeposit;

    // 이전/현재 어떤 상태인지 - 입사 신청 설정 내역과 구분을 위함
    private ApplicationStatus applicationStatus;

    @Builder
    public DormitoryApplicationSetting(Long id, String title, String securityDeposit, ApplicationStatus applicationStatus) {
        this.id = id;
        this.title = title;
        this.securityDeposit = securityDeposit;
        this.applicationStatus = applicationStatus;
    }
}
