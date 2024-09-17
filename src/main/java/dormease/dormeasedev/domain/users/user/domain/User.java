package dormease.dormeasedev.domain.users.user.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 일단 냅둠
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String loginId;

    private String password;

    // 사용자 실제 이름
    private String name;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    // Description : update 함수
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserType(UserType userType) {
        this.userType = userType;
    }

    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Builder
    public User(School school, Restaurant restaurant, String loginId, String password, String name) {
        this.school = school;
        this.restaurant = restaurant;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.userType = UserType.STUDENT;
    }
}
