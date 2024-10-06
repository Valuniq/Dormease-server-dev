package dormease.dormeasedev.domain.school_settings.standard_setting.exception;

import dormease.dormeasedev.global.exception.BusinessException;

public class StandardSettingNotFoundException extends BusinessException {

    public StandardSettingNotFoundException() {
        super("기준 설정을 찾을 수 없습니다.");
    }
}
