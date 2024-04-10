package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.request.FindLoginIdReq;
import dormease.dormeasedev.domain.user.dto.response.FindLoginIdRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    // 아이디 찾기
    public ResponseEntity<?> findLoginId(UserDetails userDetails, FindLoginIdReq findLoginIdReq) throws Exception {
        System.out.println("user details.getUsername() ============ " + userDetails.getUsername());
        Optional<User> findUser = userRepository.findByLoginId(userDetails.getUsername()); // getUsername == loginId
        if (findUser.isEmpty())
            throw new Exception("존재하지 않는 사용자입니다.");

        User user = findUser.get();

        String reqName = findLoginIdReq.getName();
        String reqPhoneNumber = findLoginIdReq.getPhoneNumber();

        if (!user.getName().equals(reqName))
            throw new Exception("이름이 일치하지 않습니다.");

        if (!user.getPhoneNumber().equals(reqPhoneNumber))
            throw new Exception("전화번호가 일치하지 않습니다.");

        User findByNameAndPhoneNumber = userRepository.findByNameAndPhoneNumber(reqName, reqPhoneNumber).get();
        FindLoginIdRes findLoginIdRes = FindLoginIdRes.builder()
                .loginId(findByNameAndPhoneNumber.getLoginId())
                .build();

        return ResponseEntity.ok(findLoginIdRes);
    }
}
