package dormease.dormeasedev.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DeleteUserInfoBySchoolRes {

    private Long id;

    private String name;

    private String studentNumber;

    // 상점
    private Integer bonusPoint;

    // 벌점
    private Integer minusPoint;

    // 탈퇴 일자
    private LocalDate deletedAt;

    @Builder
    public DeleteUserInfoBySchoolRes(Long id, String name, String studentNumber, Integer bonusPoint, Integer minusPoint, LocalDate deletedAt) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.deletedAt = deletedAt;
    }
}
