package dormease.dormeasedev.domain.resident.domain.repository;

import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
//    List<Resident> findByDormitorySettingTerm(DormitorySettingTerm settingTerm);

    List<Resident> findByRoom(Room room);

    Page<Resident> findByUserSchool(School school, Pageable pageable);

    @Query("SELECT r FROM Resident r " +
            "WHERE (r.user.school = :school AND r.user.name LIKE %:keyword%) " +
            "   OR (r.user.school = :school AND r.user.studentNumber LIKE %:keyword%)")
    Page<Resident> searchResidentsByKeyword(School school, String keyword, Pageable pageable);

    Resident findByUserAndRoom(User user, Room room);

}
