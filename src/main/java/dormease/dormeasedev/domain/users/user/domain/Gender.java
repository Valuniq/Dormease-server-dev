package dormease.dormeasedev.domain.users.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE"),
    EMPTY("EMPTY");

    private String value;
}
