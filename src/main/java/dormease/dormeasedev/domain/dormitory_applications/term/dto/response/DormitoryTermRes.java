package dormease.dormeasedev.domain.dormitory_applications.term.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryTermRes {

    @Schema(type = "Long", example = "1", description= "기숙사 ID입니다.")
    private Long dormitoryId;

    // 가격
    @Schema(type = "Integer", example = "1300000", description= "가격입니다.")
    private Integer price;

}
