package dormease.dormeasedev.domain.restaurants.menu.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MenuRes {

    @Schema(type = "List<String>", example = "[김치찌개, 볶음밥, 장조림, 불고기, 파김치, 요구르트]", description = "식당 + 날짜로 조회한 메뉴 이름 목록")
    private List<String> menuNameList;
}
