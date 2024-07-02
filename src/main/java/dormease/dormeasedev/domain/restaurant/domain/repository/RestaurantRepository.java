package dormease.dormeasedev.domain.restaurant.domain.repository;

import dormease.dormeasedev.domain.restaurant.domain.Restaurant;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllBySchool(School school);

    Optional<Restaurant> findBySchoolAndId(School school, Long restaurantId);

    Optional<Restaurant> findTopBySchoolOrderByIdDesc(School school);
}
