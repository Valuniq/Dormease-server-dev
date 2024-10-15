package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto.request.ModifyDormitorySettingTermReq;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.request.ModifyMealTicketReq;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.request.ModifyTermReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class ModifyDormitoryApplicationSettingReq {

    @Schema(type = "String", example = "2024-1학기 명지대학교 자연 생활관 재학생 2차 입사 신청", description= "입사 신청 설정의 제목입니다.")
    private String title;

    // 시작 -> 마감일 中 시작일
    @Schema(type = "local date", example = "2023-12-25", description = "입사 신청 시작일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    // 종료 -> 마감일 中 마감일
    @Schema(type = "local date", example = "2024-03-15", description = "입사 신청 마감일")
    @NotNull
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
    @Schema(type = "Integer", example = "500000", description = "보증금입니다.")
    private Integer securityDeposit;

    @Schema(type = "List<ModifyDormitorySettingTermReq>", example = "modifyDormitorySettingTermReqList", description = "입사 신청 설정에 사용되는 기숙사 정보 + 수용 인원 리스트입니다.")
    private List<ModifyDormitorySettingTermReq> modifyDormitorySettingTermReqList;

    @Schema(type = "List<ModifyTermReq>", example = "modifyTermReqList", description = "거주 기간 리스트입니다.")
    private List<ModifyTermReq> modifyTermReqList;

    @Schema(type = "List<ModifyMealTicketReq>", example = "modifyMealTicketReqList", description = "식권 리스트입니다.")
    private List<ModifyMealTicketReq> modifyMealTicketReqList;
}