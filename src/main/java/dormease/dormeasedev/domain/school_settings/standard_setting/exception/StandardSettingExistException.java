package dormease.dormeasedev.domain.school_settings.standard_setting.exception;

import lombok.Getter;

@Getter
public class StandardSettingExistException extends RuntimeException {

    public StandardSettingExistException() {
        super("기준 설정이 이미 존재합니다.");
    }
}
