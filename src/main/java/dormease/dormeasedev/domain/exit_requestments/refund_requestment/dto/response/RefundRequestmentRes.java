package dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefundRequestmentRes {

    @Schema(type = "Long", example = "1", description = "환불 신청 ID")
    private Long refundRequestmentId;

    @Schema(type = "String", example = "홍길동", description = "이름")
    private String residentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "String", example = "010-1234-5678", description = "휴대전화")
    private String phoneNumber;

    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    @Schema(type = "String", example = "9999-99-999999", description = "계좌 번호")
    private String accountNumber;

    // 거주 기간
    @Schema(type = "String", example = "6개월", description = "거주 기간")
    private String termName;

    // 퇴실 날짜
    @Schema(type = "LocalDate", example = "2024-05-02", description = "퇴실 날짜")
    private LocalDate exitDate;

    // 신청 날짜
    @Schema(type = "LocalDate", example = "2024-05-01", description = "신청 날짜")
    private LocalDate createDate;

    @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
    private String dormitoryName;

    // 인실
    @Schema(type = "Integer", example = "4인실", description = "인실")
    private Integer roomSize;

    // 호실
    @Schema(type = "Integer", example = "999호", description = "호실")
    private Integer roomNumber;

    // 침대 번호
    @Schema(type = "Integer", example = "1", description = "침대번호")
    private Integer bedNumber;
}
