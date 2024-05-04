package dormease.dormeasedev.domain.dormitory_term.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryTermRepository extends JpaRepository<DormitoryTerm, Long> {

    List<DormitoryTerm> findByTerm(String term);

    List<DormitoryTerm> findByDormitory(Dormitory dormitory);
}
