package dormease.dormeasedev.domain.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryManagementRes {

    @Schema(type = "Long", example = "1", description= "건물의 고유 id입니다.")
    private Long id;

    @Schema(type = "String", example = "명덕관(2인실)", description= "건물의 이름(호실)입니다.")
    private String name;

    @Builder
    public DormitoryManagementRes(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
