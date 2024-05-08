package dormease.dormeasedev.domain.period.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PeriodDateRes {
    
    @Schema(type = "Boolean", example = "true", description = "신청 기간이 맞는지 여부입니다. ex) true - 신청 기간 이므로 신청 가능합니다.")
    private Boolean isOverPeriod;
}
