package dormease.dormeasedev.domain.user.domain.repository;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByName(String name);

    Optional<User> findBySchoolAndStudentNumber(School school, String studentNumber);

    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);

    List<User> findBySchool(School school);

    Page<User> findBySchoolAndUserType(School school, UserType userType, Pageable pageable);

    Page<User> findBySchoolAndStatusAndUserTypeNot(School school, Status status, UserType userType, Pageable pageable);

    List<User> findBySchoolAndStatusAndUserTypeNot(School school, Status status, UserType userType);

    @Query("SELECT u FROM User u " +
            "WHERE (u.school = :school AND u.name LIKE %:keyword% AND u.status = :status AND u.userType != :userType) " +
            "   OR (u.school = :school AND u.studentNumber LIKE %:keyword% AND u.status = :status AND u.userType != :userType)")
    Page<User> searchUsersByKeyword(School school, String keyword, Status status, UserType userType, Pageable pageable);

//    Optional<User> findByRefreshToken(String refreshToken);

    /**
     * 소셜 타입과 소셜의 식별값으로 회원 찾는 메소드
     * 정보 제공을 동의한 순간 DB에 저장해야하지만, 아직 추가 정보(사는 도시, 나이 등)를 입력받지 않았으므로
     * 유저 객체는 DB에 있지만, 추가 정보가 빠진 상태이다.
     * 따라서 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
     */
//    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
