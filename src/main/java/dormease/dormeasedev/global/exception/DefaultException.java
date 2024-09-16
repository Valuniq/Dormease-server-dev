package dormease.dormeasedev.global.exception;

import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException{

    private final ErrorCode errorCode;

    public DefaultException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DefaultException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
