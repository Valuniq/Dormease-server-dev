package dormease.dormeasedev.domain.users.student.domain;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);

    Page<Student> findByUser_SchoolAndStatus(School school, Status status, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.user.school = :school AND s.status = :status AND (s.studentNumber LIKE %:studentNumberKeyword% OR s.user.name LIKE %:userNameKeyword%)")
    Page<Student> findStudentsBySchoolAndStatusAndKeyword(
            @Param("school") School school,
            @Param("status") Status status,
            @Param("studentNumberKeyword") String studentNumberKeyword,
            @Param("userNameKeyword") String userNameKeyword,
            Pageable pageable);
}
