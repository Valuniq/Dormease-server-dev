package dormease.dormeasedev.domain.school_settings.period.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class PeriodReq {

    @Schema(type = "local date", example = "2023-12-25", description = "시작일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    @Schema(type = "local date", example = "2024-03-15", description = "마감일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    @Schema(type = "PeriodType", example = "REFUND", description = "환불. LEAVE(퇴사) / REFUND(환불) / ROOMMATE(룸메이트) 中 1")
    private PeriodType periodType;

}
