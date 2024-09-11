package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DormitoryApplicationResult {

    PASS("PASS"), // 합격
    NON_PASS("NON_PASS"), // 불합격
    MOVE_PASS("MOVE_PASS"), // 이동 합격
    WAIT("WAIT"); // 대기 - 아직 결과 x


    private String value;
}
