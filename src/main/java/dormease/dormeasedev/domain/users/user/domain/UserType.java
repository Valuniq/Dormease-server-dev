package dormease.dormeasedev.domain.users.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    BLACKLIST("ROLE_BLACKLIST"),
    RESIDENT("RESIDENT");

    private String value;
}
