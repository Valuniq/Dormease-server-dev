package dormease.dormeasedev.domain.school_settings.standard_setting.exception;

public class StandardSettingNotFoundException extends RuntimeException {

    public StandardSettingNotFoundException() {
        super("기준 설정을 찾을 수 없습니다.");
    }
}
