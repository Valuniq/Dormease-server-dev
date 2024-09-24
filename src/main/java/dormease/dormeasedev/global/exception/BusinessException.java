package dormease.dormeasedev.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

//    @Builder
//    public BusinessException(String message, ErrorCode errorCode) {
//        super(message);
//        this.errorCode = errorCode;
//    }
//
//    @Builder
//    public BusinessException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }
}
