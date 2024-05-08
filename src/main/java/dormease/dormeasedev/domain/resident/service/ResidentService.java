package dormease.dormeasedev.domain.resident.service;

import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.global.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResidentService {

    private final ResidentRepository residentRepository;


    // Description : 유효성 검증 함수
    public Resident validateResidentByUser(User user) {
        Optional<Resident> findResident = residentRepository.findByUser(user);
        DefaultAssert.isTrue(findResident.isPresent(), "해당 유저는 사생이 아닙니다.");
        return findResident.get();
    }

    public Resident validateResidentById(Long residentId) {
        Optional<Resident> findResident = residentRepository.findById(residentId);
        DefaultAssert.isTrue(findResident.isPresent(), "해당 아이디의 사생이 존재하지 않습니다.");
        return findResident.get();
    }
}
