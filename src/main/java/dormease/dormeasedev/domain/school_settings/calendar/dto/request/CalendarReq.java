package dormease.dormeasedev.domain.school_settings.calendar.dto.request;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalendarReq {

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 시작일자입니다.")
    @NotNull
    private LocalDate startDate;

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 종료일자입니다.")
    private LocalDate endDate;

    @Schema(type = "String", example = "수강신청", description= "일정의 제목입니다.")
    @NotBlank
    @Size(max = 30, message = "제목은 최대 30자까지 가능합니다.")
    private String title;

    @Schema(type = "String", example = "9일: 인문대학, 10일: ICT융합대학/경영대학, 11일: 법과대학/사회과학대학", description= "일정의 내용입니다.")
    @Size(max = 2000, message = "내용은 최대 2000자까지 가능합니다.")
    private String content;

    @Schema(type = "String", example = "GREY", description= "일정의 색깔입니다. GREY, RED, GREEN, YELLOW, BLUE만 입력 가능합니다.")
    private Color color;

}
