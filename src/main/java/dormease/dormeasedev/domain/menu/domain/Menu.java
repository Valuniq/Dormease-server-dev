package dormease.dormeasedev.domain.menu.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // 음식 이름
    private String name;

    // 날짜
    private LocalDate date;

    @Builder
    public Menu(Long id, Restaurant restaurant, String name, LocalDate date) {
        this.id = id;
        this.restaurant = restaurant;
        this.name = name;
        this.date = date;
    }
}
