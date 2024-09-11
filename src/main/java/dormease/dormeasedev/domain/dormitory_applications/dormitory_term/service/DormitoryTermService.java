package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.global.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryTermService {

    private final DormitoryTermRepository dormitoryTermRepository;



    // Description : 유효성 검증 함수
    public DormitoryTerm validateDormitoryTermById(Long dormitoryTermId) {
        Optional<DormitoryTerm> findDormitoryTerm = dormitoryTermRepository.findById(dormitoryTermId);
        DefaultAssert.isTrue(findDormitoryTerm.isPresent(), "존재하지 않는 id 입니다.");
        return findDormitoryTerm.get();
    }
    public DormitoryTerm validateDormitoryTermByTermAndDormitory(Term term, Dormitory dormitory) {
        Optional<DormitoryTerm> findDormitoryTerm = dormitoryTermRepository.findByTermAndDormitory(term, dormitory);
        DefaultAssert.isTrue(findDormitoryTerm.isPresent(), "연관되지 않은 거주기간과 기숙사 정보입니다.");
        return findDormitoryTerm.get();
    }
}
