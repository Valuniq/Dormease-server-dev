package dormease.dormeasedev.domain.users.blacklist.domain.repository;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.blacklist.domain.BlackList;
import dormease.dormeasedev.domain.users.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    BlackList findByStudent(Student student);

    Page<BlackList> findByStudent_School(School school, Pageable pageable);
}
