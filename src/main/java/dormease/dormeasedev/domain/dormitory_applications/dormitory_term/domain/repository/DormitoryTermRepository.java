package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository;

import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryTermRepository extends JpaRepository<DormitoryTerm, Long> {

    List<DormitoryTerm> findByTerm(Term term);

    List<DormitoryTerm> findByDormitoryRoomType(DormitoryRoomType dormitoryRoomType);

    List<DormitoryTerm> findByDormitoryRoomTypeIn(List<DormitoryRoomType> dormitoryRoomTypes);

    boolean existsByDormitoryRoomTypeIn(List<DormitoryRoomType> dormitoryRoomTypes);
}
