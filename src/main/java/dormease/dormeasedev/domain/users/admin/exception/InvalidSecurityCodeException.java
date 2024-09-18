package dormease.dormeasedev.domain.users.admin.exception;

public class InvalidSecurityCodeException extends RuntimeException {

    public InvalidSecurityCodeException() {
        super("유효하지 않은 보안 코드입니다.");
    }
}