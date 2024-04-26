package dormease.dormeasedev.domain.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryManagementListRes {

    @Schema(type = "Long", example = "1", description= "건물의 고유 id입니다.")
    private Long id;

    @Schema(type = "String", example = "명덕관", description= "건물의 이름입니다.")
    private String name;

    @Builder
    public DormitoryManagementListRes(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
