package dormease.dormeasedev.domain.dormitories.dormitory.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
    List<Dormitory> findBySchool(School school);

    List<Dormitory> findBySchoolAndName(School school, String name);

    // @Modifying
    // @Query("UPDATE Dormitory d SET d.imageUrl = :imageUrl WHERE d.school = :school AND d.name = :name")
    // void updateImageUrlForSchoolAndDorm(School school, String name, String imageUrl);

    // @Modifying
    // @Query("UPDATE Dormitory d SET d.name = :newName WHERE d.school = :school AND d.name = :oldName")
    // void updateNamesBySchoolAndName(School school, String oldName, String newName);

    //Dormitory findBySchoolAndNameAndGenderAndRoomSize(School school, String name, Gender gender, Integer roomSize);

    // 학교, 성별, 거주 기간에 따른 기숙사 목록 조회
//    @Query("SELECT d FROM Dormitory d " +
//            "INNER JOIN DormitorySettingTerm dst ON d.id = dst.dormitory.id " +
//            "INNER JOIN Term dt ON dst.dormitoryTerm.id = dt.id " +
//            "WHERE d.school.id = :schoolId " +
//            "AND d.gender = :gender " +
//            "AND dt.term = :term")
//    List<Dormitory> findDormitoriesBySchoolIdAndGenderAndTerm(Long schoolId, Gender gender, String term);

    //List<Dormitory> findDormitoriesBySchoolIdAndGender(Long id, Gender gender);
    //List<Dormitory> findBySchoolAndNameAndRoomSize(School school, String name, Integer roomSize);

    //List<Dormitory> findBySchoolAndNameAndGender(School school, String name, Gender gender);

    List<Dormitory> findBySchoolOrderByCreatedDateAsc(School school);

    boolean existsBySchoolAndName(School school, String dormitoryName);

    int countBySchool(School school);
}
