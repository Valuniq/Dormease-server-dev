package dormease.dormeasedev.domain.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryResidentAssignmentRes {

    @Schema(type = "Long", example = "1", description = "기숙사의 id입니다.")
    private Long dormitoryId;

    @Schema(type = "String", example = "인문생활관", description= "사생이 거주 가능한 건물명입니다.")
    private String dormitoryName;

    @Schema(type = "Integer", example = "2", description= "사생이 거주 가능한 건물의 인실입니다.")
    private Integer roomSize;

    @Builder
    public DormitoryResidentAssignmentRes(Long dormitoryId, String dormitoryName, Integer roomSize) {
        this.dormitoryId = dormitoryId;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
    }
}
