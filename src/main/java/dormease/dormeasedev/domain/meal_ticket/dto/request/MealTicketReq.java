package dormease.dormeasedev.domain.meal_ticket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MealTicketReq {

    @Schema(type = "Long", example = "1", description = "식권 ID")
    private Long id;

    @Schema(type = "Integer", example = "50", description= "식권 개수입니다.")
    private Integer count;

    @Schema(type = "Integer", example = "1200000", description= "총 식권 가격입니다.")
    private Integer price;

}
