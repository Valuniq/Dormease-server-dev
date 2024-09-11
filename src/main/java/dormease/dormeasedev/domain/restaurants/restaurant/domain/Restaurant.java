package dormease.dormeasedev.domain.restaurants.restaurant.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Restaurant extends BaseEntity {
    
    // TODO : Menu를 MenuController 등 만들어서 쓸지, Restaurant에서 MappedBy로만 사용할지 생각 - 일단 나눴음 (Controller 사용 방식으로)
    // TODO : 참고) 비긴비건 Food - FoodIngredient 관계

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private String name;

    @Builder
    public Restaurant(Long id, School school, String name) {
        this.id = id;
        this.school = school;
        this.name = name;
    }
}
