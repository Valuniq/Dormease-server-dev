package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryReq;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.request.MealTicketReq;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.request.TermReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CreateDormitoryApplicationSettingReq {

    @Schema(type = "Long", example = "1", description = "학교 ID")
    private Long schoolId;

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
    @Schema(type = "Integer", example = "500000", description= "보증금입니다.")
    private Integer securityDeposit;

    @Schema(type = "List<DormitoryReq>", example = "dormitoryReqList", description= "입사 신청 설정에 사용되는 기숙사 정보 리스트입니다.")
    private List<DormitoryReq> dormitoryReqList = new ArrayList<>(); // dormitory id, 수용인원(Patch. 여기서 저장하는 것인듯)

    @Schema(type = "List<TermReq>", example = "termReqList", description= "거주 기간 리스트입니다.")
    private List<TermReq> termReqList = new ArrayList<>();

    @Schema(type = "List<MealTicketReq>", example = "mealTicketReqList", description= "거주 기간 별 가격 및 입.퇴사 날짜 리스트입니다.")
    private List<MealTicketReq> mealTicketReqList = new ArrayList<>();

}
