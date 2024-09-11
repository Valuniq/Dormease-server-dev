package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CopyRoomsReq {

    @NotNull
    @Schema(type = "Integer", example = "1", description= "복제할 호실의 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer originalFloor;

    @NotNull
    @Schema(type = "Integer", example = "1", description= "생성하려는 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer newFloor;

}
