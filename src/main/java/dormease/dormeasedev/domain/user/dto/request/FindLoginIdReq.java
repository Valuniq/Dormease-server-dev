package dormease.dormeasedev.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindLoginIdReq {

    @Schema(type = "String", example = "강승우", description= "유저의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "01012345678", description= "전화번호입니다.")
    private String phoneNumber;

}
