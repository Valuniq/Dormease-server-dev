package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryManagementDetailRes {

    // @Schema(type = "Long", example = "1", description= "건물의 고유 id입니다.")
    // private Long id;

    @Schema(type = "String", example = "명덕관", description= "건물의 이름(호실)입니다.")
    private String name;

    @Schema(type = "String", example = "https://dormease-s3-bucket.s3.amazonaws.com/014edc6a-48d0-4f11-8379-a4e48ba61402.jpg", description= "건물 이미지 URL입니다.")
    private String imageUrl;

    // 꽉 찬 방 개수
    @Schema(type = "Integer", description= "인원이 꽉 찬 호실의 수입니다.")
    private Integer fullRoomCount;

    @Schema(type = "Integer", description= "활성화된 호실의 수입니다..")
    private Integer roomCount;

    // 현재 수용된 인원
    @Schema(type = "Integer", description= "현재 수용인원 수입니다.")
    private Integer currentPeopleCount;

    @Schema(type = "Integer", description= "건물(+인실)의 수용 인원입니다.")
    private Integer dormitorySize;

    @Schema(type = "String", example = "101호 층간소음", description= "건물별 메모입니다.")
    private String memo;

    @Builder
    public DormitoryManagementDetailRes(String name, String imageUrl, Integer fullRoomCount, Integer roomCount, Integer currentPeopleCount, Integer dormitorySize, String memo) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.fullRoomCount = fullRoomCount;
        this.roomCount = roomCount;
        this.currentPeopleCount = currentPeopleCount;
        this.dormitorySize = dormitorySize;
        this.memo = memo;
    }
}
