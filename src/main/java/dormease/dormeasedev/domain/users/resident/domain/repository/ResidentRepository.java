package dormease.dormeasedev.domain.users.resident.domain.repository;

import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.User;
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

    List<Resident> findByRoom(Room room);

    Page<Resident> findBySchool(School school, Pageable pageable);

    @Query("SELECT r FROM Resident r " +
            "LEFT JOIN r.student s " +
            "WHERE r.school = :school " +
            "AND (" +
            "   r.name LIKE %:keyword% " +
            "   OR (s.studentNumber LIKE %:keyword%)" +
            ")")
    Page<Resident> searchResidentsByKeyword(School school, String keyword, Pageable pageable);

    Optional<Resident> findByStudent(Student student);

    boolean existsByStudent(Student student);

    List<Resident> findByRoommateTempApplication(RoommateTempApplication roommateTempApplication);

//    @Query("SELECT r FROM Resident r JOIN r.room rm JOIN rm.dormitory d WHERE d IN :dormitories")
//    List<Resident> findByDormitories(List<Dormitory> dormitories);

    boolean existsByRoomAndBedNumber(Room room, int i);

//    List<Resident> findByDormitoryAndRoom(Dormitory dormitory, Room room);

//    @Query("SELECT r FROM Resident r WHERE r.user IN :users")
//    Page<Resident> findResidentsByUsers(@Param("users") List<User> users, Pageable pageable);
    @Query("SELECT r FROM Resident r WHERE r.student IN :students")
    Page<Resident> findResidentsByStudents(@Param("students") List<Student> students, Pageable pageable);

    Optional<Resident> findByStudent_User(User user);

//    @Query("SELECT r FROM Resident r WHERE r.user IN :users")
//    Page<Resident> findResidentsByUsers(@Param("users") List<User> users, Pageable pageable);

//    boolean existsByDormitory(Dormitory dormitory);

    boolean existsByRoomIn(List<Room> rooms);

    boolean existsByDormitoryTermIn(List<DormitoryTerm> dormitoryTermList);

    List<Resident> findByDormitoryTermAndRoom(DormitoryTerm dormitoryTerm, Room room);

    boolean existsByRoom(Room room);

    Integer countByRoom(Room room);
}
