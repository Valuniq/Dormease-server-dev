package dormease.dormeasedev.domain.school_settings.standard_setting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FreshmanStandard {

    EVERYONE("EVERYONE", "전원 합격"),
    LONG_DISTANCE("LONG_DISTANCE", "장거리 우선 합격")
    ;

    String key;
    String value;
}
