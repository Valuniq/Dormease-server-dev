package dormease.dormeasedev.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ModifyStudentNumberReq {

    @Schema(type = "String", example = "60190927", description= "사용자의 학번입니다. (혹은 수험번호)")
    private String studentNumber;

}
