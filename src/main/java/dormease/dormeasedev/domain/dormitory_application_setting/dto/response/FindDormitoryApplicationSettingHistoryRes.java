package dormease.dormeasedev.domain.dormitory_application_setting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FindDormitoryApplicationSettingHistoryRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID")
    private Long dormitoryApplicationSettingId;

    @Schema(type = "String", example = "2024-1학기 명지대학교 자연 생활관 재학생 2차 입사 신청", description= "입사 신청 설정의 제목입니다.")
    private String title;

    @Schema(type = "StartDate", example = "2023-12-25", description = "시작일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    @Schema(type = "endDate", example = "2024-01-25", description = "마감일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

}
