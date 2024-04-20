package dormease.dormeasedev.domain.dormitory.dto.response;

import dormease.dormeasedev.domain.dormitory_term.dto.response.DormitoryTermRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryForFindDormitoryApplicationSettingRes {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer dormitorySize; // 수용 인원

    @Schema(type = "List<DormitoryTermRes>", example = "DormitoryTermResList", description= ".")
    private List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();
}
