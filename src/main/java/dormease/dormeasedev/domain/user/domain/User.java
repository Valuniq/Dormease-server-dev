package dormease.dormeasedev.domain.user.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.dto.request.ModifyStudentNumberReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends BaseEntity {

    /**
     *  TODO : 생각할 점
     *   - Address String 하나로 저장할지? / 시,군,구 등 구분할지
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private String loginId;

    private String password;

    // 사용자 실제 이름
    private String name;

    private String phoneNumber;

    private String studentNumber;

    private Boolean alarmSetting;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserType userType;

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

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.userType = UserType.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // Description : update 함수
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateUserType(UserType userType) {
        this.userType = userType;
    }

    //    public void updateRefreshToken(String updateRefreshToken) {
//        this.refreshToken = updateRefreshToken;
//    }

    @Builder
    public User(Long id, School school, String loginId, String password, String name, String phoneNumber, String studentNumber, Boolean alarmSetting, Gender gender, UserType userType, Integer bonusPoint, Integer minusPoint, SchoolStatus schoolStatus, String address, String major, Integer schoolYear, Double grade) {
        this.id = id;
        this.school = school;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.studentNumber = studentNumber;
        this.alarmSetting = true;
        this.gender = gender;
        this.userType = userType;
        this.bonusPoint = 0;
        this.minusPoint = 0;
        this.schoolStatus = schoolStatus;
        this.address = address;
        this.major = major;
        this.schoolYear = schoolYear;
        this.grade = grade;
    }

}
