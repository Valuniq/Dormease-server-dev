package dormease.dormeasedev.domain.users.auth.exception;

import dormease.dormeasedev.global.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
