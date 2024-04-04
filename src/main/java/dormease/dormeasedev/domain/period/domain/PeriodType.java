package dormease.dormeasedev.domain.period.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PeriodType {

    JOIN("JOIN"), // 입사
    LEAVE("LEAVE"), // 퇴사
    REFUND("REFUND"), // 환불
    ROOMMATE("ROOMMATE"), // 룸메이트
    DEPOSIT("DEPOSIT"); // 입금


    private String value;
}
