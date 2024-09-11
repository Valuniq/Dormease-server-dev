package dormease.dormeasedev.domain.school_settings.calendar.dto.response;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CalendarRes {

    @Schema(type = "Long", example = "1", description= "일정 id입니다.")
    private Long calendarId;

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 시작일자입니다.")
    private LocalDate startDate;

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 종료일자입니다.")
    private LocalDate endDate;

    @Schema(type = "String", example = "수강신청", description= "일정의 제목입니다.")
    private String title;

    @Schema(type = "String", example = "GREY", description= "일정의 색깔입니다. GREY, RED, GREEN, YELLOW, BLUE만 입력 가능합니다.")
    private Color color;

    @Builder
    public CalendarRes(Long calendarId, LocalDate startDate, LocalDate endDate, String title, Color color) {
        this.calendarId = calendarId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.color = color;
    }
}
