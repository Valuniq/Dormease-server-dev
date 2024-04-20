package dormease.dormeasedev.domain.period.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.period.domain.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PeriodRes {

    @Schema(type = "Long", example = "1", description = "기간 ID")
    private Long periodId;

    @Schema(type = "local date", example = "2023-12-25", description = "시작일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    @Schema(type = "local date", example = "2024-03-15", description = "마감일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    @Schema(type = "PeriodType", example = "DEPOSIT", description = "입금")
    private PeriodType periodType;

}
