package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterDormitoryReq {

    @NotBlank
    @Schema(type = "String", example = "명덕관", description= "건물의 이름입니다.")
    private String name;
}
