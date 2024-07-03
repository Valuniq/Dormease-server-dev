package dormease.dormeasedev.domain.dormitory_application_setting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_setting_term.dto.DormitorySettingTermRes;
import dormease.dormeasedev.domain.dormitory_term.dto.TermRes;
import dormease.dormeasedev.domain.meal_ticket.dto.response.MealTicketRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FindDormitoryApplicationSettingRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID")
    private Long dormitoryApplicationSettingId;

    @Schema(type = "String", example = "2024-1학기 명지대학교 자연 생활관 재학생 2차 입사 신청", description= "입사 신청 설정의 제목입니다.")
    private String title;

    // 시작 -> 마감일 中 시작일
    @Schema(type = "local date", example = "2023-12-25", description = "입사 신청 시작일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    // 시작 -> 마감일 中 마감일
    @Schema(type = "local date", example = "2024-03-15", description = "입사 신청 마감일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    @Schema(type = "local date", example = "2024-03-15", description = "입금 시작일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate depositStartDate;

    @Schema(type = "local date", example = "2024-03-15", description = "입금 마감일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate depositEndDate;

    // 보증금
    @Schema(type = "Integer", example = "500000", description= "보증금입니다.")
    private Integer securityDeposit;

    // 이전/현재 어떤 상태인지 - 입사 신청 설정 내역과 구분을 위함
    @Schema(type = "ApplicationStatus", example = "NOW", description= "내역으로 들어갈지 / 앞으로 사용할 것인지 결정합니다.")
    private ApplicationStatus applicationStatus;

    @Schema(type = "List<DormitorySettingTermRes>", example = "DormitorySettingTermRes 객체", description= "기숙사 정보 리스트입니다.")
    private List<DormitorySettingTermRes> dormitorySettingTermResList = new ArrayList<>();

    @Schema(type = "List<TermRes>", example = "TermRes 객체", description= "거주 기간 정보 리스트입니다.")
    private List<TermRes> termResList = new ArrayList<>();

    @Schema(type = "List<MealTicketRes>", example = "List<MealTicketRes>", description= "식권 정보 리스트입니다.")
    private List<MealTicketRes> mealTicketResList = new ArrayList<>();

}
