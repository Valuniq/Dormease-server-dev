package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DormitoryReq {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer acceptLimit; // 수용 인원
}
