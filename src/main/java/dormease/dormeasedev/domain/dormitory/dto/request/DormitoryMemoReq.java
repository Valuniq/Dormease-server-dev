package dormease.dormeasedev.domain.dormitory.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DormitoryMemoReq {

    @Size(max = 200, message = "최대 200자까지 입력가능합니다.")
    private String memo;
}
