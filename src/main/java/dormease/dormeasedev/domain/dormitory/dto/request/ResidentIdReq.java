package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResidentIdReq {

    @NotNull
    @Schema(type = "Long", example = "1", description= "사생의 고유 id입니다.")
    private Long id;
}