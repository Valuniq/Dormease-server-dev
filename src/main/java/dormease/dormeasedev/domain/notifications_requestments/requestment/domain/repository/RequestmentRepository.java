package dormease.dormeasedev.domain.notifications_requestments.requestment.domain.repository;

import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.Requestment;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestmentRepository extends JpaRepository<Requestment, Long> {

    // APP
    Page<Requestment> findRequestmentsByStudent_User_SchoolAndVisibility(School school, Boolean visibility, Pageable pageable);

    Optional<Requestment> findByIdAndUser_School(Long requestmentId, School school);

    Optional<Requestment> findByIdAndStudent(Long requestmentId, Student student);

    Page<Requestment> findRequestmentsByStudent(Student student, Pageable pageable);

    // WEB
    Page<Requestment> findRequestmentsByUser_School(School school, Pageable pageable);

}
