package dormease.dormeasedev.domain.dormitory_term_relation.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_term_relation.domain.DormitoryTermRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryTermRelationRepository extends JpaRepository<DormitoryTermRelation, Long> {
    List<DormitoryTermRelation> findByDormitory(Dormitory dormitory);
}
