package dormease.dormeasedev.domain.users.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ActiveUserInfoRes {

    @Schema(type = "Long", example = "1", description= "회원의 id입니다.")
    private Long id;

    @Schema(type = "String", example = "사용자", description= "회원의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "회원의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "전화번호", description= "회원의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "Integer", example = "1", description= "회원의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "회원의 벌점 총점입니다.")
    private Integer minusPoint;

    // 생성 일자
    @Schema(type = "LocalDate", example = "2024-04-26", description = "가입 일자입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
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
