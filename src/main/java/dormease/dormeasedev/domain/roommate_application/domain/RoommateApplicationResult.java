package dormease.dormeasedev.domain.roommate_application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoommateApplicationResult {

    PASS("PASS"), // 합격
    NON_PASS("NON_PASS"), // 불합격
    WAITING("WAITING"); // 대기

    private String value;
}
