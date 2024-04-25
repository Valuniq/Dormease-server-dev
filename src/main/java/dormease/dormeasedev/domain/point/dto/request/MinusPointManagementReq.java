package dormease.dormeasedev.domain.point.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MinusPointManagementReq {

    @Size(max = 30, message = "최대 30자까지 입력 가능합니다.")
    private String content;

    private Integer score;
}
