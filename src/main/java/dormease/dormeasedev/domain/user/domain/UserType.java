package dormease.dormeasedev.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    BLACKLIST("ROLE_BLACKLIST");

    private String value;
}

