package dormease.dormeasedev.domain.users.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SchoolStatus {
    ENROLLMENT("ENROLLMENT"), // 재학
    LEAVE_OF_ABSENCE("LEAVE_OF_ABSENCE"), // 휴학
    EXPULSION("EXPULSION"), // 제적
    GRADUATE("GRADUATE"), // 졸업
    GRADUATE_DEFERRAL("GRADUATE_DEFERRAL"), // 학위 취득 유예
    COMPLETION("COMPLETION") // 수료

    ;

    private String value;
}
