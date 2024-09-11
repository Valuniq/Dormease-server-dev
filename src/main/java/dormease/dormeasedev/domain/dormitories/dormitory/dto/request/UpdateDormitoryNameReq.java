package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateDormitoryNameReq {

    @Schema(type = "String", example = "명덕관", description= "건물의 이름입니다.")
    @NotBlank
    @Size(max = 10, message = "건물의 이름은 최대 10글자까지 입력 가능합니다.")
    private String name;
}
