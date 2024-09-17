package dormease.dormeasedev.domain.users.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN("ROLE_ADMIN", "관리자"),
    STUDENT("ROLE_STUDENT", "학생") // 일반 회원
//    BLACKLIST("ROLE_BLACKLIST", "블랙리스트"),
//    RESIDENT("RESIDENT", "사생")
    ;

    String key;
    String value;
}

