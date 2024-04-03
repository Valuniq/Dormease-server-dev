package dormease.dormeasedev.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN("ADMIN"),
    USER("USER"),
    BLACKLIST("BLACKLIST");

    private String value;
}

