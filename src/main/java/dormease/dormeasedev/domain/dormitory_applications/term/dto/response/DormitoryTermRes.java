package dormease.dormeasedev.domain.dormitory_applications.term.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryTermRes {

    @Schema(type = "Long", example = "1", description= "기숙사(인실 구분)과 거주기간을 연결짓는 테이블의 ID입니다. 화면 상 가격이 적히는 부분의 ID입니다.")
    private Long dormitoryTermId;

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitoryRoomTypeId;

    // 가격
    @Schema(type = "Integer", example = "1300000", description= "가격입니다.")
    private Integer price;

}
