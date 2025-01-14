package dormease.dormeasedev.domain.dormitories.dormitory.domain;

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
public class Dormitory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

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
    public Dormitory(School school, String name, String memo, String imageUrl, Integer dormitorySize, Integer roomCount) {
        this.school = school;
        this.name = name;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.dormitorySize = dormitorySize;
        this.roomCount = roomCount;
    }

    public void updateImageUrl(String imagePath) {
        this.imageUrl = imagePath;
    }

    public void updateRoomCount(Integer count) {
        this.roomCount = count;
    }

    public void updateDormitorySize(Integer dormitorySize) {
        this.dormitorySize = dormitorySize;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
