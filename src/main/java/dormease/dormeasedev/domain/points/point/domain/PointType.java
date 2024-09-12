package dormease.dormeasedev.domain.points.point.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PointType {
    BONUS("BONUS"),
    MINUS("MINUS");

    private String value;

}
