package dormease.dormeasedev.domain.users.user.exception;

import dormease.dormeasedev.global.exception.BusinessException;

public class InvalidSchoolAuthorityException extends BusinessException {

    public InvalidSchoolAuthorityException() {
        super("해당 학교에 대한 권한이 없습니다.");
    }
}
