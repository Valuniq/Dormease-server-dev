package dormease.dormeasedev.domain.room.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.user.domain.Gender;
import dormease.dormeasedev.domain.user.domain.User;
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
    
    // 호수
    private Integer roomNumber;

    // 층수
    private Integer floor;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    // 인실
    private Integer roomSize;
    
    // 열쇠 수령 여부
    private Boolean hasKey;
    
    // 현재 인원
    private Integer currentPeople;
    
    // 활성화 여부
    private Boolean isActivated;

    @Builder
    public Room(Long id, Dormitory dormitory, Integer roomNumber, Integer floor, Gender gender, Integer roomSize, Boolean hasKey, Integer currentPeople, Boolean isActivated) {
        this.id = id;
        this.dormitory = dormitory;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.gender = gender;
        this.roomSize = roomSize;
        this.hasKey = hasKey;
        this.currentPeople = currentPeople;
        this.isActivated = true;
    }

    public void updateRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }
    public void updateFloor(Integer floor) { this.floor = floor; }
    public void updateCurrentPeople(Integer currentPeople) { this.currentPeople = currentPeople; }

    // num만큼 room의 currentPeople를 빼거나 더해서 업데이트
    public void adjustRoomCurrentPeople(Room room, int num) {
        int count = room.getCurrentPeople() + num;
        updateCurrentPeople(Math.max(count, 0));
    }

    public void updateRoomSetting(Dormitory dormitory, Integer roomSize, Gender gender, Boolean hasKey) {
        this.dormitory = dormitory;
        this.roomSize = roomSize;
        this.gender = gender;
        this.hasKey = hasKey;
    }

    public void updateIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }
}
