package dormease.dormeasedev.domain.dormitory.dto.response.app_dormitory_application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindDormitoryRes {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    private String dormitoryName;

    private Integer roomSize;

    private String imageUrl;

    private Integer price;

    @Builder
    public FindDormitoryRes(Long dormitoryId, String dormitoryName, Integer roomSize, String imageUrl, Integer price) {
        this.dormitoryId = dormitoryId;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.imageUrl = imageUrl;
        this.price = price;
    }
}