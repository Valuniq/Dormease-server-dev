package dormease.dormeasedev.domain.dormitory_term.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryTermNameRes {

    @Schema(type = "Long", example = "1", description = "거주 기간 id입니다.")
    private Long dormitoryTermId;

    @Schema(type = "String", example = "학기", description= "거주 기간입니다.")
    private String term;

}
