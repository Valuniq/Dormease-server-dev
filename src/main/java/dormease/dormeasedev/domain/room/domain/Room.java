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
    
    private Gender gender;
    
    // 인실
    private Integer roomSize;
    
    // 열쇠 수령 여부
    private Boolean hasKey;
    
    // 현재 인원
    private Integer currentPeople;
    
    // 활성화 여부
    private Boolean isActivated = true;

    @Builder
    public Room(Long id, Dormitory dormitory, Integer roomNumber, Gender gender, Integer roomSize, Boolean hasKey, Integer currentPeople, Boolean isActivated) {
        this.id = id;
        this.dormitory = dormitory;
        this.roomNumber = roomNumber;
        this.gender = gender;
        this.roomSize = roomSize;
        this.hasKey = hasKey;
        this.currentPeople = currentPeople;
        this.isActivated = isActivated;
    }
}
