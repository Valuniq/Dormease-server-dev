package dormease.dormeasedev.domain.dormitories.room_type.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RoomType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 인실
    private Integer roomSize;

    @Builder
    public RoomType(Gender gender, Integer roomSize) {
        this.gender = gender;
        this.roomSize = roomSize;
    }
}
