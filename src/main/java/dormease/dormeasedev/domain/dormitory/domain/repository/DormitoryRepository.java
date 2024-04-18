package dormease.dormeasedev.domain.dormitory.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
    List<Dormitory> findBySchool(School school);

    List<Dormitory> findBySchoolAndName(School school, String name);

    @Modifying
    @Query("UPDATE Dormitory d SET d.imageUrl = :imageUrl WHERE d.school = :school AND d.name = :name")
    void updateImageUrlForSchoolAndDorm(School school, String name, String imageUrl);

    @Modifying
    @Query("UPDATE Dormitory d SET d.name = :newName WHERE d.school = :school AND d.name = :oldName")
    void updateNamesBySchoolAndName(School school, String oldName, String newName);

    Dormitory findBySchoolAndNameAndGenderAndRoomSize(School school, String name, Gender gender, Integer roomSize);
}
