package dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryRoomTypeRepository extends JpaRepository<DormitoryRoomType, Long> {
    List<DormitoryRoomType> findByDormitory(Dormitory dormitory);

    boolean existsByDormitoryAndRoomTypeGender(Dormitory dormitory, Gender gender);

    List<DormitoryRoomType> findByDormitoryAndRoomTypeGender(Dormitory dormitory, Gender gender);
}