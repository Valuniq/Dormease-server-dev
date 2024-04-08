package dormease.dormeasedev.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SchoolStatus {
    ENROLLMENT("ENROLLMENT"), // 재학
    LEAVE_OF_ABSENCE("LEAVE_OF_ABSENCE"), // 휴학
    EXPULSION("EXPULSION"); // 제적

    private String value;
}
