package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationSimpleRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "2024학년도 1학기 1차 정기 신청", description = "입사 신청 설정 제목")
    private String dormitoryApplicationSettingTitle;

    @Schema(type = "local date", example = "2024-01-01", description = "입사 신청 제출일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;
}
