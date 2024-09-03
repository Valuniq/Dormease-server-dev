package dormease.dormeasedev.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DeleteUserInfoRes {

    @Schema(type = "Long", example = "1", description= "탈퇴한 회원의 id입니다.")
    private Long id;

    @Schema(type = "String", example = "사용자", description= "탈퇴한 회원의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "탈퇴한 회원의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "Integer", example = "1", description= "탈퇴한 회원의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "탈퇴한 회원의 벌점 총점입니다.")
    private Integer minusPoint;

    // 탈퇴 일자
    @Schema(type = "LocalDate", example = "2024-04-26", description = "탈퇴 일자입니다.")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate deletedAt;

    @Builder
    public DeleteUserInfoRes(Long id, String name, String studentNumber, Integer bonusPoint, Integer minusPoint, LocalDate deletedAt) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.deletedAt = deletedAt;
    }
}
