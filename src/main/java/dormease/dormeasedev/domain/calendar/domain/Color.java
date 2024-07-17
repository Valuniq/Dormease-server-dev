package dormease.dormeasedev.domain.calendar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Color {

    GREY("GREY"),
    RED("RED"),
    GREEN("GREEN"),
    YELLOW("YELLOW"),
    BLUE("BLUE");

    private String value;
}
