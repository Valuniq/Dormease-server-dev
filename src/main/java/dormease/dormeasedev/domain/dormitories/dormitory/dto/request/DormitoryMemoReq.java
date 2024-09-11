package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DormitoryMemoReq {

    @Schema(type = "String", example = "101호 층간소음", description= "건물별 메모입니다.")
    @Size(max = 200, message = "최대 200자까지 입력 가능합니다.")
    private String memo;
}
