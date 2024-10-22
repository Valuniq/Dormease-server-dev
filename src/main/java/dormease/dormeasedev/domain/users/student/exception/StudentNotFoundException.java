package dormease.dormeasedev.domain.users.student.exception;

import dormease.dormeasedev.global.exception.BusinessException;

public class StudentNotFoundException extends BusinessException {

    public StudentNotFoundException() {
        super("학생을 찾을 수 없습니다.");
    }
}
