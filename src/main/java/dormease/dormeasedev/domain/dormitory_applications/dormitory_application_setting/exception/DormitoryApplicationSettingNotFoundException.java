package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception;

public class DormitoryApplicationSettingNotFoundException extends RuntimeException {

    public DormitoryApplicationSettingNotFoundException() {
        super("입사 신청 설정을 찾을 수 없습니다.");
    }
}
