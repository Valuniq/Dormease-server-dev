package dormease.dormeasedev.domain.users.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BlackListUserInfoRes {

    @Schema(type = "Long", example = "1", description= "회원의 id입니다.")
    private Long id;

    @Schema(type = "String", example = "사용자", description= "회원의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "회원의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "전화번호", description= "회원의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "Integer", example = "1", description= "회원의 벌점 총점입니다.")
    private Integer minusPoint;

    @Schema(type = "String", example = "관내 흡연", description= "회원의 블랙리스트 등록 사유입니다.")
    private String content;

    // 생성 일자
    @Schema(type = "LocalDate", example = "2024-04-26", description = "블랙리스트 등록 일자입니다.")
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
