package dormease.dormeasedev.domain.users.student.domain;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.SchoolStatus;
import dormease.dormeasedev.domain.users.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@SuperBuilder
public class Student extends User {

    /**
     *  TODO : 생각할 점
     *   - Address String 하나로 저장할지? / 시,군,구 등 구분할지
     */

    // Student
    private String phoneNumber;

    private String studentNumber;

    private Boolean alarmSetting;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 상점
    private Integer bonusPoint;

    // 벌점
    private Integer minusPoint;

    // 학적
    @Enumerated(EnumType.STRING)
    private SchoolStatus schoolStatus;

    // 거주지
    private String address;

    // 학과
    private String major;

    // 학년 : 아마 1~6학년 예상
    private Integer schoolYear;

    // 성적
    private Double grade;

    public void updateStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateBonusPoint(Integer bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public void updateMinusPoint(Integer minusPoint) {
        this.minusPoint = minusPoint;
    }
}
