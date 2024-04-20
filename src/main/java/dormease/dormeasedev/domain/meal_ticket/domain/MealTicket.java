package dormease.dormeasedev.domain.meal_ticket.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.period.domain.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MealTicket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_ticket_id")
    private Long id;

    // 입사 신청 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitoryApplicationSetting_id")
    private DormitoryApplicationSetting dormitoryApplicationSetting;

    private Integer count;

    private Integer price;

    @Builder
    public MealTicket(Long id, DormitoryApplicationSetting dormitoryApplicationSetting, Integer count, Integer price) {
        this.id = id;
        this.dormitoryApplicationSetting = dormitoryApplicationSetting;
        this.count = count;
        this.price = price;
    }
}
