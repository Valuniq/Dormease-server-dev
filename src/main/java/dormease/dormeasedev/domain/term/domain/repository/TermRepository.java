package dormease.dormeasedev.domain.term.domain.repository;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findByTermName(String termName);

//    List<Term> findByDormitory(Dormitory dormitory);

    List<Term> findByDormitoryApplicationSetting(DormitoryApplicationSetting dormitoryApplicationSetting);
}
