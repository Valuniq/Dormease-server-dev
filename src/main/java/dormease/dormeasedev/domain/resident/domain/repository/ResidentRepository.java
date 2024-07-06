package dormease.dormeasedev.domain.resident.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
//    List<Resident> findByDormitorySettingTerm(DormitorySettingTerm settingTerm);

    List<Resident> findByRoom(Room room);

    Page<Resident> findBySchool(School school, Pageable pageable);

    @Query("SELECT r FROM Resident r " +
            "LEFT JOIN r.user u " +
            "WHERE r.school = :school " +
            "AND (" +
            "   r.name LIKE %:keyword% " +
            "   OR (u.studentNumber LIKE %:keyword%)" +
            ")")
    Page<Resident> searchResidentsByKeyword(School school, String keyword, Pageable pageable);

    Optional<Resident> findByUser(User user);

    List<Resident> findByRoommateTempApplication(RoommateTempApplication roommateTempApplication);

    Resident findByUserAndRoom(User user, Room room);

    @Query("SELECT r FROM Resident r JOIN r.room rm JOIN rm.dormitory d WHERE d IN :dormitories")
    List<Resident> findByDormitories(List<Dormitory> dormitories);

    boolean existsByRoomAndBedNumber(Room room, int i);

    @Query("SELECT r FROM Resident r WHERE r.user IN :users")
    Page<Resident> findResidentsByUsers(@Param("users") List<User> users, Pageable pageable);

}
