package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormitoryTermRepository extends JpaRepository<DormitoryTerm, Long> {

    List<DormitoryTerm> findByTerm(Term term);

//    Optional<DormitoryTerm> findByTermAndDormitory(Term term, Dormitory dormitory);
}
