package dormease.dormeasedev.domain.dormitories.room.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    List<Room> findByDormitory(Dormitory dormitory);

    List<Room> findByDormitoryAndIsActivated(Dormitory dormitory, boolean b);

    List<Room> findByDormitoryAndFloorAndIsActivated(Dormitory dormitory, Integer floor, boolean b);

    List<Room> findByDormitoryInAndFloor(List<Dormitory> sameNameDormitories, Integer floor);
}
