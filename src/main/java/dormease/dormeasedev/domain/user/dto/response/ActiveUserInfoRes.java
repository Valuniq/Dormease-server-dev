package dormease.dormeasedev.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ActiveUserInfoRes {

    private Long id;

    private String name;

    private String studentNumber;

    // 휴대전화
    private String phoneNumber;

    // 상점
    private Integer bonusPoint;

    // 벌점
    private Integer minusPoint;

    // 생성 일자
    private LocalDate createdAt;

    @Builder
    public ActiveUserInfoRes(Long id, String name, String studentNumber, String phoneNumber, Integer bonusPoint, Integer minusPoint, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.createdAt = createdAt;
    }
}
