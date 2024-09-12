package dormease.dormeasedev.domain.restaurants.menu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class FindMenuReq {

    // TODO : 날짜, 식당 id,

    @Schema(type = "Long", example = "1", description= "메뉴를 조회할 식당의 id입니다.")
    private Long restaurantId;

    @Schema(type = "local date", example = "2024-03-15", description = "메뉴를 조회할 날짜입니다.")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate menuDate;
}
