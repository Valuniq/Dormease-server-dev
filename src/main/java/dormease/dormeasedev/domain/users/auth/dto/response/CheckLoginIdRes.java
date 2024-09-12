package dormease.dormeasedev.domain.users.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CheckLoginIdRes {

    @Schema(type = "Boolean", example = "true" , description = "true : 중복, 사용 불가입니다.")
    private Boolean isDuplicate;

}
