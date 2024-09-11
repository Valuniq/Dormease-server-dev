package dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecurityDepositReturnStatus {

    PAYMENT("PAYMENT"), // 지급
    UNPAID("UNPAID"), // 미지급
    UNALBE("UNABLE"); // 지급 불가

    private String value;
}
