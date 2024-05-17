package dormease.dormeasedev.domain.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitorySettingListRes {

    @Schema(type = "Long", example = "1", description= "건물의 고유 id입니다.")
    private Long id;

    @Schema(type = "String", example = "명덕관", description= "건물의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "https://dormease-s3-bucket.s3.amazonaws.com/014edc6a-48d0-4f11-8379-a4e48ba61402.jpg", description= "건물 이미지 URL입니다.")
    private String imageUrl;

    @Schema(type = "boolean", example = "true/false", description= "해당 건물에 배정된 사생이 있는지 확인합니다.")
    private boolean assignedResidents;

    @Builder
    public DormitorySettingListRes(Long id, String name, String imageUrl, boolean assignedResidents) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.assignedResidents = assignedResidents;
    }
}
