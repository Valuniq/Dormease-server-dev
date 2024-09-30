package dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MainRefundRequestmentRes {

    @Schema(type = "Long", example = "1", description = "환불 신청 ID")
    private Long refundRequestmentId;

    @Schema(type = "String", example = "홍길동", description = "이름")
    private String residentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
    private String dormitoryName;

    @Schema(type = "Integer", example = "999", description = "호실")
    private Integer roomNumber;

    @Schema(type = "LocalDate", example = "2024-05-01", description = "신청 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createDate;
}
