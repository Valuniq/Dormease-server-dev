package dormease.dormeasedev.domain.dormitory_setting_relation.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.period.domain.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitorySettingRelation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_setting_relation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_ticket_id")
    private MealTicket mealTicket;

    // 거주 기간 별 입/퇴사 날짜
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private Period period;

    @Builder
    public DormitorySettingRelation(Long id, Dormitory dormitory, MealTicket mealTicket, Period period) {
        this.id = id;
        this.dormitory = dormitory;
        this.mealTicket = mealTicket;
        this.period = period;
    }
}
