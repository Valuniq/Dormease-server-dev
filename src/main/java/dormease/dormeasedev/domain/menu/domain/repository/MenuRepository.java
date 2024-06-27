package dormease.dormeasedev.domain.menu.domain.repository;

import dormease.dormeasedev.domain.menu.domain.Menu;
import dormease.dormeasedev.domain.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByRestaurantAndMenuDate(Restaurant restaurant, LocalDate date);
}
