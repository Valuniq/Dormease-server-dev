package dormease.dormeasedev.domain.users.resident.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResidentDormitoryInfoReq {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "Integer", example = "2", description= "사생이 거주하는 건물의 인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "1", description= "사생이 거주하는 호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "Integer", example = "1", description= "사생의 호실 침대번호 입니다.")
    private Integer bedNumber;

    @Schema(type = "Long", example = "1", description = "거주기간 ID")
    private Long termId;
}
