package dormease.dormeasedev.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BlackListUserInfoRes {

    private Long id;

    private String name;

    private String studentNumber;

    // 휴대전화
    private String phoneNumber;

    // 벌점
    private Integer minusPoint;

    // 사유
    private String content;

    // 생성 일자
    private LocalDate createdAt;

    @Builder
    public BlackListUserInfoRes(Long id, String name, String studentNumber, String phoneNumber, Integer minusPoint, String content, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.minusPoint = minusPoint;
        this.content = content;
        this.createdAt = createdAt;
    }
}
