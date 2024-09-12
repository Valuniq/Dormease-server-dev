package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddRoomNumberReq {

    @NotNull
    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer floor;

    @NotNull
    @Schema(type = "Integer", example = "1~99", description= "호실의 시작 번호입니다. 1이상 99이하이며, endRoomNumber보다 작아야합니다.")
    @Min(value = 1, message = "호실은 최소 1이상이어야 합니다.")
    @Max(value = 99, message = "호실은 최대 2자리 수까지 가능합니다.")
    private Integer startRoomNumber;

    @NotNull
    @Schema(type = "Integer", example = "1~99", description= "호실의 끝 번호입니다. 1이상 99이하이며, startRoomNumber보다 커야합니다.")
    @Min(value = 1, message = "호실은 최소 1이상이어야 합니다.")
    @Max(value = 99, message = "호실은 최대 2자리 수까지 가능합니다.")
    private Integer endRoomNumber;
}
