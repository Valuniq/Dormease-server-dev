package dormease.dormeasedev.domain.school_settings.calendar.dto.response;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodayCalendarRes {

    @Schema(type = "Long", example = "1", description= "일정 id입니다.")
    private Long calendarId;

    @Schema(type = "String", example = "수강신청", description= "일정의 제목입니다.")
    private String title;

    @Schema(type = "String", example = "GREY", description= "일정의 색깔입니다. GREY, RED, GREEN, YELLOW, BLUE만 입력 가능합니다.")
    private Color color;

    @Builder
    public TodayCalendarRes(Long calendarId, String title, Color color) {
        this.calendarId = calendarId;
        this.title = title;
        this.color = color;
    }
}
