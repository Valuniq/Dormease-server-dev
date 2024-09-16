package dormease.dormeasedev.domain.school.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school.domain.repository.SchoolRepository;
import dormease.dormeasedev.global.exception.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;


    // Description : 유효성 검증 함수
    public School validateSchoolById(Long schoolId) {
        Optional<School> findSchool = schoolRepository.findById(schoolId);
        DefaultAssert.isTrue(findSchool.isPresent(), "학교 정보가 올바르지 않습니다.");
        return findSchool.get();
    }
}
