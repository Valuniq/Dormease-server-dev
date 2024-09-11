package dormease.dormeasedev.domain.users.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FindLoginIdRes {

    @Schema(type = "String", example = "phonil", description= "로그인 시 사용될 아이디입니다.")
    private String loginId;
}
