package dormease.dormeasedev.domain.dormitories.dormitory.dto.response.app_dormitory_application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindDormitoryRes {

    @Schema(type = "Long", example = "1", description= "기숙사(인실 구분)과 거주기간을 연결짓는 테이블의 ID입니다. 가격이 포함되어 있습니다.")
    private Long dormitoryTermId;

    @Schema(type = "String", example = "www.example.com", description = "기숙사 이미지 url")
    private String imageUrl;

    @Schema(type = "String", example = "명덕관", description = "기숙사 이름")
    private String dormitoryName;

    @Schema(type = "Integer", example = "4", description = "기숙사 인실")
    private Integer roomSize;

    @Schema(type = "Integer", example = "600000", description = "기숙사 거주 기간별 가격")
    private Integer price;

    @Schema(type = "String", example = "학기", description = "거주 기간")
    private String termName;

    @Builder
    public FindDormitoryRes(Long dormitoryTermId, String dormitoryName, Integer roomSize, String imageUrl, Integer price, String termName) {
        this.dormitoryTermId = dormitoryTermId;
        this.imageUrl = imageUrl;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.price = price;
        this.termName = termName;
    }
}
