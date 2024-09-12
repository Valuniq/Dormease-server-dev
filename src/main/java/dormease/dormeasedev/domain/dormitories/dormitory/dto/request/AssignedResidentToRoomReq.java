package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AssignedResidentToRoomReq {

    @NotNull
    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long roomId;

    @NotNull
    @Schema(type = "array", example = "[1, 2, 3]", description= "사생의 고유 id 배열입니다.")
    private Long[] residentIds;
}
