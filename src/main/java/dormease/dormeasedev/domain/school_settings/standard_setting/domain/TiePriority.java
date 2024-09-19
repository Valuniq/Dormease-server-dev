package dormease.dormeasedev.domain.school_settings.standard_setting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TiePriority {

    SCORE("SCORE", "성적"),
    DISTANCE("DISTANCE", "거리 점수")
    ;

    String key;
    String value;
}
