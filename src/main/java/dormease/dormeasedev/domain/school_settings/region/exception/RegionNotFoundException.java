package dormease.dormeasedev.domain.school_settings.region.exception;

import dormease.dormeasedev.global.exception.BusinessException;

public class RegionNotFoundException extends BusinessException {

    public RegionNotFoundException() {
        super("지역을 찾을 수 없습니다.");
    }
}
