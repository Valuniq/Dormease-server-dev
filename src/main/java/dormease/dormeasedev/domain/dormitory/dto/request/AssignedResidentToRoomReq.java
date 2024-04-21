package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class AssignedResidentToRoomReq {

    @NotBlank
    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long roomId;

    // 리스트로 사생 id
    @ArraySchema(schema = @Schema(type = "Long", example = "1", description= "사생의 고유 id입니다."))
    private List<ResidentIdReq> residentIdReqList;
}
