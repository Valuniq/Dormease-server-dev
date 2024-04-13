package dormease.dormeasedev.domain.room.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    List<Room> findByDormitory(Dormitory dormitory);
}
