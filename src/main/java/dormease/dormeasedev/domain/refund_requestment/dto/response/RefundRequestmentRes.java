package dormease.dormeasedev.domain.refund_requestment.dto.response;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefundRequestmentRes {

    // TODO : 사생 이름, 학번, 휴대전화, 은행명, 계좌번호, (거주)기간, 퇴사 예정일(퇴실 날짜), 신청날짜, 건물(호실 포함), 호실, 침대번호

    private Long refundRequestmentId;

    private String studentNumber;

    private String phoneNumber;

    private String bankName;

    private String accountNumber;

    // 거주 기간
    private String term;

    // 퇴실 날짜
    private LocalDate exitDate;

    // 신청 날짜
    private LocalDate createDate;

    private String dormitoryName;

    // 인실
    private Integer roomSize;

    // 호실
    private Integer roomNumber;

    // 침대 번호
    private Integer bedNumber;
}
