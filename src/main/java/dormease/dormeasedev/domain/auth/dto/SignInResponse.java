package dormease.dormeasedev.domain.auth.dto;

import dormease.dormeasedev.domain.user.domain.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SignInResponse {

    @Schema(type = "String", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g" , description= "Access Token을 출력합니다.")
    private String accessToken;

    @Schema(type = "String", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.asdf8as4df865as4dfasdf65_asdfweioufsdoiuf_432jdsaFEWFSDV_sadf" , description= "Refresh Token을 출력합니다.")
    private String refreshToken;

    @Schema(type = "UserType", example = "USER", description = "유저 타입을 출력합니다. EXHIBITION, REVIEW, REPLY 中 1")
    private UserType userType;

}
