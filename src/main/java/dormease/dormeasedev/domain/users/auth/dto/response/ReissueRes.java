package dormease.dormeasedev.domain.users.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReissueRes {

    @Schema(
            type = "string",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsInJvbGUiOiJjdnZ6M0BuYXZlci5jb20iLCJpZCI6ImN2dnozQG5hdmVyLmNvbSIsImV4cCI6MTcyMzYyODQ2MiwiZW1haWwiOiJjdnZ6M0BuYXZlci5jb20ifQ.b8_v-GTWTFxQVmhH1jg-JUERpVXGe_tFg4-Tjv6F8DtymMMKgxicCF6dWlsHxJEhyKL3k-Z4qnCeVq_EcH54eg",
            description= "새로 발급된 access token을 출력합니다."
    )
    private String accessToken;
}
