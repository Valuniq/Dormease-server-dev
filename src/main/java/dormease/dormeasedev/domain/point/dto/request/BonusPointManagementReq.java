package dormease.dormeasedev.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BonusPointManagementReq {

    @Size(max = 30, message = "최대 30자까지 입력 가능합니다.")
    private String content;

    @NotNull
    @Schema(type = "Integer", example = "1~99", description= "호실의 시작 번호입니다. 1이상 99이하이며, endRoomNumber보다 작아야합니다.")
    @Min(value = 1, message = "점수는 최소 1이상이어야 합니다.")
    @Max(value = 99, message = "점수는 최대 2자리 수까지 가능합니다.")
    private Integer score;
}
