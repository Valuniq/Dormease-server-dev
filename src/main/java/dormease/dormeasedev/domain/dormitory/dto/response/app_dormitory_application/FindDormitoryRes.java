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

    @Schema(type = "String", example = "명덕관", description = "기숙사 이름")
    private String dormitoryName;

    @Schema(type = "Integer", example = "4", description = "기숙사 인실")
    private Integer roomSize;

    @Schema(type = "String", example = "www.example.com", description = "기숙사 이미지 url")
    private String imageUrl;

    @Schema(type = "Integer", example = "600000", description = "기숙사 거주 기간별 가격")
    private Integer price;

    @Schema(type = "String", example = "학기", description = "거주 기간")
    private String term;

    @Builder
    public FindDormitoryRes(Long dormitoryId, String dormitoryName, Integer roomSize, String imageUrl, Integer price, String term) {
        this.dormitoryId = dormitoryId;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.imageUrl = imageUrl;
        this.price = price;
        this.term = term;
    }
}
