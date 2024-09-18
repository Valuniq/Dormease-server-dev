package dormease.dormeasedev.domain.dormitories.room.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    
    // 호수
    private Integer roomNumber;

    // 층수
    private Integer floor;

    // 열쇠 수령 여부
    private Boolean hasKey;
    
    // 현재 인원
    private Integer currentPeople;
    
    // 활성화 여부
    private Boolean isActivated;

    @Builder
    public Room(Dormitory dormitory, Boolean isActivated, RoomType roomType, Integer roomNumber, Integer floor, Boolean hasKey, Integer currentPeople) {
        this.dormitory = dormitory;
        this.isActivated = isActivated;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.hasKey = hasKey;
        this.currentPeople = currentPeople;
        this.isActivated = true;
    }

    public void updateCurrentPeople(Integer currentPeople) { this.currentPeople = currentPeople; }

    // num만큼 room의 currentPeople를 빼거나 더해서 업데이트
    public void adjustRoomCurrentPeople(Room room, int num) {
        int count = room.getCurrentPeople() + num;
        updateCurrentPeople(Math.max(count, 0));
    }

    public void updateRoomAttributes(RoomType roomType, Boolean hasKey, Boolean isActivated) {
        this.roomType = roomType;
        this.hasKey = hasKey;
        this.isActivated = isActivated;
    }

}
