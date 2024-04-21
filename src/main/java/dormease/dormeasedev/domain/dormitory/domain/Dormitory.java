package dormease.dormeasedev.domain.dormitory.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryReq;
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

    // 기숙사 건물 이름
    @Column(nullable = false)
    private String name;

    private String memo;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 인실
    private Integer roomSize;

    // 대표 사진
    private String imageUrl;

    // 수용 인원
    private Integer dormitorySize;

    // 방 개수
    private Integer roomCount;

    // 거주 기간
    private String term;

    private Integer price;

    // 시작 -> 마감일 中 시작일
    private LocalDate startDate;

//     시작 -> 마감일 中 마감일
    private LocalDate endDate;

    @Builder
    public Dormitory(Long id, School school, String name, String memo, Gender gender, Integer roomSize, String imageUrl, Integer dormitorySize, Integer roomCount, String term, Integer price, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.school = school;
        this.name = name;
        this.memo = memo;
        this.gender = gender;
        this.roomSize = roomSize;
        this.imageUrl = imageUrl;
        this.dormitorySize = dormitorySize;
        this.roomCount = roomCount;
        this.term = term;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void updateGenderAndRoomSize(Gender gender, Integer roomSize) {
        this.gender = gender;
        this.roomSize = roomSize;
    }
}
