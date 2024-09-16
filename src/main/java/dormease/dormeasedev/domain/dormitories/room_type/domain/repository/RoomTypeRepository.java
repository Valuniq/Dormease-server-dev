package dormease.dormeasedev.domain.dormitories.room_type.domain.repository;

import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    RoomType findByRoomSizeAndGender(Integer roomSize, Gender gender);

}
