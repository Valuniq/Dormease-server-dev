package dormease.dormeasedev.domain.restaurants.restaurant.service;

import dormease.dormeasedev.domain.restaurants.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.restaurants.restaurant.domain.repository.RestaurantRepository;
import dormease.dormeasedev.domain.restaurants.restaurant.dto.response.RestaurantNameRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserService userService;

    public ResponseEntity<?> findRestaurantList(UserDetailsImpl userDetailsImpl) {

        User user = userService.validateUserById(userDetailsImpl.getId());
        School school = user.getSchool();

        List<Restaurant> restaurantList = restaurantRepository.findAllBySchool(school);
        List<RestaurantNameRes> restaurantNameResList = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            RestaurantNameRes restaurantNameRes = RestaurantNameRes.builder()
                    .restaurantId(restaurant.getId())
                    .name(restaurant.getName())
                    .build();
            restaurantNameResList.add(restaurantNameRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(restaurantNameResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
