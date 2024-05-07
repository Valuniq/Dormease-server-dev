package dormease.dormeasedev.domain.resident.domain.repository;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
//    List<Resident> findByDormitorySettingTerm(DormitorySettingTerm settingTerm);

    List<Resident> findByRoom(Room room);

//    List<Resident> findByDormitorySettingTermAndRoom(DormitorySettingTerm dormitorySettingTerm, Room room);

    Page<Resident> findByUserSchool(School school, Pageable pageable);

    @Query("SELECT r FROM Resident r " +
            "WHERE (r.user.school = :school AND r.user.name LIKE %:keyword%) " +
            "   OR (r.user.school = :school AND r.user.studentNumber LIKE %:keyword%)")
    Page<Resident> searchResidentsByKeyword(School school, String keyword, Pageable pageable);

    Optional<Resident> findByUser(User user);

    List<Resident> findByRoommateRempApplication(RoommateTempApplication roommateTempApplication);
}
