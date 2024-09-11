package dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MealTicketRes {

    @Schema(type = "Long", example = "1", description = "식권 ID")
    private Long id;

    @Schema(type = "Integer", example = "50", description= "식권 개수입니다.")
    private Integer count;

    @Schema(type = "Integer", example = "1200000", description= "총 식권 가격입니다.")
    private Integer price;
}
