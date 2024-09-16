package dormease.dormeasedev.domain.restaurants.menu.controller;

import dormease.dormeasedev.domain.restaurants.menu.dto.request.FindMenuReq;
import dormease.dormeasedev.domain.restaurants.menu.dto.response.MenuRes;
import dormease.dormeasedev.domain.restaurants.menu.service.MenuAppService;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Menu API", description = "메뉴 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/menus")
public class MenuAppController {

    private final MenuAppService menuAppService;

    // Description : 식당 + 날짜로 메뉴 조회
    @Operation(summary = "식당 + 날짜로 메뉴 목록 조회", description = "식당 + 날짜로 메뉴 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MenuRes.class))}),
            @ApiResponse(responseCode = "400", description = "메뉴 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findMenuList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 FindMenuReq를 참고해주세요.", required = true) @Valid @RequestBody FindMenuReq findMenuReq
    ) {
        return menuAppService.findMenuList(customUserDetails, findMenuReq);
    }
}
