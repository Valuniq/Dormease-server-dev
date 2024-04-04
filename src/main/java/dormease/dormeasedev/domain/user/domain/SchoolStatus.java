package dormease.dormeasedev.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SchoolStatus {
    ENROLLMENT("ENROLLMENT"),
    LEAVE_OF_ABSENCE("LEAVE_OF_ABSENCE"),
    EXPULSION("EXPULSION");

    private String value;
}
