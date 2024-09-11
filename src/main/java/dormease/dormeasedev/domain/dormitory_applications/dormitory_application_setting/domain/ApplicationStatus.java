package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationStatus {

    BEFORE("BEFORE"), // 이전
    NOW("NOW"); // 현재

    private String value;
}
