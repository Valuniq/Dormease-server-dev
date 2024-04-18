package dormease.dormeasedev.domain.dormitory_application_setting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationSettingRes {

    @Schema(type = "String", example = "2024-1학기 명지대학교 자연 생활관 재학생 2차 입사 신청", description= "입사 신청 설정의 제목입니다.")
    private String title;

    // 시작 -> 마감일 中 시작일
    @Schema(type = "local date", example = "2023-12-25", description = "시작일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    // 시작 -> 마감일 中 마감일
    @Schema(type = "local date", example = "2024-03-15", description = "마감일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    // 보증금
    @Schema(type = "Integer", example = "500000", description= "보증금입니다.")
    private Integer securityDeposit;

    // 이전/현재 어떤 상태인지 - 입사 신청 설정 내역과 구분을 위함
    @Schema(type = "ApplicationStatus", example = "NOW", description= "내역으로 들어갈지 / 앞으로 사용할 것인지 결정합니다.")
    private ApplicationStatus applicationStatus;

}
