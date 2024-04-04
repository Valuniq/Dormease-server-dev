package dormease.dormeasedev.domain.meal_ticket.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
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

    private Integer count;

    private Integer price;

    @Builder
    public MealTicket(Long id, Integer count, Integer price) {
        this.id = id;
        this.count = count;
        this.price = price;
    }
}
