package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DormitoryTermReq {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    // 가격
    @Schema(type = "Integer", example = "1300000", description= "가격입니다.")
    private Integer price;
}
