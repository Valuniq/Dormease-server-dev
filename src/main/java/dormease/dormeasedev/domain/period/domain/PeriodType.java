package dormease.dormeasedev.domain.period.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PeriodType {

    LEAVE("LEAVE"), // 퇴사
    REFUND("REFUND"), // 환불
    ROOMMATE("ROOMMATE"); // 룸메이트

    private String value;
}
