package dormease.dormeasedev.domain.restaurants.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RestaurantNameRes {

    @Schema(type = "Long", example = "1", description = "식당 ID")
    private Long restaurantId;

    @Schema(type = "String", example = "학생회관", description = "식당 건물 이름")
    private String name;
}
