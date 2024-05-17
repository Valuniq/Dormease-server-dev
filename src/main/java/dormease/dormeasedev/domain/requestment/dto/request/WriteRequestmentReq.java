package dormease.dormeasedev.domain.requestment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WriteRequestmentReq {

    @Schema(type = "String", example = "보일러가 안켜져요", description = "요청사항 제목")
    private String title;

    @Schema(type = "String", example = "어제부터 보일러가 안켜지는데 확인 부탁드립 ..", description = "요청사항 내용")
    @Lob
    private String content;

    @Schema(type = "String", example = "true", description = "부재 중 방문 동의 여부")
    private Boolean consentDuringAbsence;

    @Schema(type = "String", example = "true", description = "공개 여부")
    private Boolean visibility;

}
