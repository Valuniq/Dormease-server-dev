package dormease.dormeasedev.domain.users.student.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.roommates.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.users.auth.dto.request.ModifyReq;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.SchoolStatus;
import dormease.dormeasedev.domain.users.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Student extends BaseEntity {

    /**
     *  TODO : 생각할 점
     *   - Address String 하나로 저장할지? / 시,군,구 등 구분할지
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roommate_application_id")
    private RoommateApplication roommateApplication;

    // Student
    private String phoneNumber;

    private String studentNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean alarmSetting;

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

    public void update(ModifyReq modifyReq) {
        this.phoneNumber = modifyReq.getPhoneNumber();
        this.address = modifyReq.getAddress();
        this.studentNumber = modifyReq.getStudentNumber();
        this.schoolStatus = modifyReq.getSchoolStatus();
        this.major = modifyReq.getMajor();
        this.schoolYear = modifyReq.getSchoolYear();
        this.grade = modifyReq.getGrade();
    }

    @Builder
    public Student(User user, Region region, String phoneNumber, String studentNumber, Gender gender, SchoolStatus schoolStatus, String address, String major, Integer schoolYear, Double grade) {
        this.user = user;
        this.region = region;
        this.roommateApplication = null;
        this.phoneNumber = phoneNumber;
        this.studentNumber = studentNumber;
        this.gender = gender;
        this.alarmSetting = true;
        this.bonusPoint = 0;
        this.minusPoint = 0;
        this.schoolStatus = schoolStatus;
        this.address = address;
        this.major = major;
        this.schoolYear = schoolYear;
        this.grade = grade;
    }
}
