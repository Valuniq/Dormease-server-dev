package dormease.dormeasedev.domain.users.user.exception;

import lombok.Getter;


public class InvalidSchoolAuthorityException extends RuntimeException {

    public InvalidSchoolAuthorityException() {
        super("해당 학교에 대한 권한이 없습니다.");
    }
}
