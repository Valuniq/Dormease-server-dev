package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ModifyDormitoryTermReq {

    @Schema(type = "Long", example = "1", description= "기숙사(인실 구분)과 거주기간을 연결짓는 테이블의 ID입니다. 화면 상 가격이 적히는 부분의 ID입니다.")
    private Long dormitoryTermId;

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitoryRoomTypeId;

    @Schema(type = "Integer", example = "1300000", description= "가격입니다.")
    private Integer price;
}
