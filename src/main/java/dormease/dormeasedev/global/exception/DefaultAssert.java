package dormease.dormeasedev.global.exception;

import org.springframework.util.Assert;

import java.util.Optional;

public class DefaultAssert extends Assert {

    public static void isTrue(boolean value){
        if(!value){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }
    }

    public static void isTrue(boolean value, String message){
        if(!value){
            throw new DefaultException(ErrorCode.INVALID_CHECK, message);
        }
    }

    /**
     * Optional의 값이 존재하지 않을 경우 IllegalArgumentException을 발생시키고,
     * 값이 존재할 경우 Optional의 값을 반환
     *
     * @param value Optional 값
     * @param message Optional 값이 존재하지 않을 경우 발생할 예외 메시지
     * @param <T> Optional 값의 타입
     * @return Optional의 값
     * @throws IllegalArgumentException Optional 값이 존재하지 않을 경우
     */
//    public static <T> T isOptionalPresent(Optional<T> value, String message) {
//        if (!value.isPresent()) {
//            throw new IllegalArgumentException(message);
//        }
//        return value.get();
//    }
}
