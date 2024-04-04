package dormease.dormeasedev.domain.calendar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Color {

    RED("RED"),
    GREEN("GREEN"),
    BLUE("BLUE");

    private String value;
}
