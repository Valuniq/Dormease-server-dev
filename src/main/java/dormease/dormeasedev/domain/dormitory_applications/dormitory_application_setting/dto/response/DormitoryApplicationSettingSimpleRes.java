package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationSettingSimpleRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID")
    private Long dormitoryApplicationSettingId;

    @Schema(type = "String", example = "2024-1학기 명지대학교 자연 생활관 재학생 2차 입사 신청", description= "입사 신청 설정의 제목입니다.")
    private String title;

    @Schema(type = "LocalDate", example = "2024-07-25", description = "등록 일자")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;
}
