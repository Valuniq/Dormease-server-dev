package dormease.dormeasedev.domain.dormitory_term.domain.repository;

import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DormitoryTermRepository extends JpaRepository<DormitoryTerm, Long> {
}
