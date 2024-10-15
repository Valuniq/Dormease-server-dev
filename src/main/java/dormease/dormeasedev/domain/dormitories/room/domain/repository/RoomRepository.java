package dormease.dormeasedev.domain.dormitories.room.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    List<Room> findByDormitory(Dormitory dormitory);

    List<Room> findByDormitoryAndIsActivated(Dormitory dormitory, boolean b);

    List<Room> findByDormitoryAndFloorAndIsActivated(Dormitory dormitory, Integer floor, boolean b);

    @Query("SELECT MAX(r.roomNumber) FROM Room r WHERE r.dormitory = :dormitory AND r.floor = :floor")
    Integer findMaxRoomNumberByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    @Query("SELECT MIN(r.roomNumber) FROM Room r WHERE r.dormitory = :dormitory AND r.floor = :floor")
    Integer findMinRoomNumberByDormitoryAndFloor(Dormitory dormitory, Integer floor);

    Integer countByDormitoryAndIsActivated(Dormitory dormitory, boolean b);

    Integer countByDormitoryAndIsActivatedAndRoomType(Dormitory dormitory, boolean b, RoomType roomType);

    void deleteByDormitory(Dormitory dormitory);

    Room findByDormitoryAndRoomNumber(Dormitory dormitory, Integer roomNumber);
}
