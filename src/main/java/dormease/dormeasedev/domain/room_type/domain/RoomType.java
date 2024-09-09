package dormease.dormeasedev.domain.room_type.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.user.domain.Gender;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_room_type_id")
    private DormitoryRoomType dormitoryRoomType;

    @Builder
    public RoomType(Long id, Gender gender, Integer roomSize, DormitoryRoomType dormitoryRoomType) {
        this.id = id;
        this.gender = gender;
        this.roomSize = roomSize;
        this.dormitoryRoomType = dormitoryRoomType;
    }

    // public void updateGenderAndRoomSize(Gender gender, Integer roomSize) {
    //     this.gender = gender;
    //    this.roomSize = roomSize;
    // }

}
