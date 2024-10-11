package dormease.dormeasedev.domain.users.auth.dto.request;

import dormease.dormeasedev.domain.users.user.domain.SchoolStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JoinReq {

    // 이름
    private String name;
    // 전화번호
    private String phoneNumber;
    // 주소
    private String address;
    // 학번
    private String studentNumber;
    // 학적
    private SchoolStatus schoolStatus;
    // 학과
    private String major;
    // 학년
    private Integer schoolYear;
    // 학점
    private Double grade;
}
