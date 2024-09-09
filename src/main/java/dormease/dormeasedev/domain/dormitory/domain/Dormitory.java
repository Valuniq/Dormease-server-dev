package dormease.dormeasedev.domain.dormitory.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryReq;
import dormease.dormeasedev.domain.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Dormitory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_room_type_id")
    private DormitoryRoomType dormitoryRoomType;

    // 기숙사 건물 이름
    @Column(nullable = false)
    private String name;

    private String memo;

    // 대표 사진
    private String imageUrl;

    // 수용 인원
    private Integer dormitorySize;

    // 방 개수
    private Integer roomCount;

    @Builder
    public Dormitory(Long id, School school, DormitoryRoomType dormitoryRoomType, String name, String memo, String imageUrl, Integer dormitorySize, Integer roomCount) {
        this.id = id;
        this.school = school;
        this.dormitoryRoomType = dormitoryRoomType;
        this.name = name;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.dormitorySize = dormitorySize;
        this.roomCount = roomCount;
    }

    public void updateImageUrl(String imagePath) {
        this.imageUrl = imagePath;
    }

    public void updateDormitorySize(Integer dormitorySize) {
        this.dormitorySize = dormitorySize;
    }

    public void updateRoomCount(Integer count) {
        this.roomCount = count;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

}
