package dormease.dormeasedev.domain.room.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    List<Room> findByDormitory(Dormitory dormitory);

    List<Room> findByDormitoryAndIsActivated(Dormitory dormitory, boolean b);

    Page<Room> findByDormitoryAndFloorAndIsActivated(Dormitory dormitory, Integer floor, boolean b, Pageable pageable);

    Page<Room> findByDormitoryInAndFloor(List<Dormitory> sameNameDormitories, Integer floor, Pageable pageable);
}
