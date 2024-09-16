package dormease.dormeasedev.domain.restaurants.menu.service;

import dormease.dormeasedev.domain.restaurants.menu.domain.Menu;
import dormease.dormeasedev.domain.restaurants.menu.domain.repository.MenuRepository;
import dormease.dormeasedev.domain.restaurants.menu.dto.request.FindMenuReq;
import dormease.dormeasedev.domain.restaurants.menu.dto.response.MenuRes;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.repository.RestaurantRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.exception.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuAppService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    private final UserService userService;

    public ResponseEntity<?> findMenuList(CustomUserDetails customUserDetails, FindMenuReq findMenuReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        Optional<Restaurant> findRestaurant = restaurantRepository.findById(findMenuReq.getRestaurantId());
        DefaultAssert.isTrue(findRestaurant.isPresent(), "존재하지 않는 식당 id입니다.");
        Restaurant restaurant = findRestaurant.get();

        DefaultAssert.isTrue(restaurant.getSchool().equals(user.getSchool()), "본인이 소속된 학교만 조회할 수 있습니다.");
        List<Menu> menuList = menuRepository.findAllByRestaurantAndMenuDate(restaurant, findMenuReq.getMenuDate());
        List<MenuRes> menuResList = new ArrayList<>();
        List<String> menuNameList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuNameList.add(menu.getName());
        }

        MenuRes menuRes = MenuRes.builder()
                .menuNameList(menuNameList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(menuRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
