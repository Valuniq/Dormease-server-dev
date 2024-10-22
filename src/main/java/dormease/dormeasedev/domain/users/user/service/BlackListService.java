package dormease.dormeasedev.domain.users.user.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.blacklist.domain.BlackList;
import dormease.dormeasedev.domain.users.blacklist.domain.repository.BlackListRepository;
import dormease.dormeasedev.domain.users.blacklist.dto.request.BlackListContentReq;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.repository.StudentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.dto.response.BlackListUserInfoRes;
import dormease.dormeasedev.domain.users.user.exception.InvalidSchoolAuthorityException;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageInfo;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BlackListService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BlackListRepository blackListRepository;

    private final UserService userService;

    // 블랙리스트 목록 조회
    public ResponseEntity<?> getBlackListUsers(UserDetailsImpl userDetailsImpl, Integer page) {
        User adminUser = validUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<BlackList> blackListPage = blackListRepository.findByStudent_User_School(school, pageable);

        // 목록 조회 및 페이징
//        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.DESC, "createdDate"));
//        Page<User> blackListedUsersPage = userRepository.findBySchoolAndUserType(admin.getSchool(), UserType.BLACKLIST, pageable);

        List<BlackListUserInfoRes> blackListUserInfoRes = blackListPage.getContent().stream()
                .map(blackList -> {
                    Student student = blackList.getStudent();
                    return BlackListUserInfoRes.builder()
                            .id(blackList.getId())
                            .name(student.getUser().getName())
                            .studentNumber(student.getStudentNumber())
                            .phoneNumber(student.getPhoneNumber())
                            .minusPoint(student.getMinusPoint())
                            .content(blackList.getContent())
                            .createdAt(blackList.getCreatedDate().toLocalDate())
                            .build();
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, blackListPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, blackListUserInfoRes);

        // ApiResponse 객체 생성 및 반환
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 블랙리스트 사유 작성
    @Transactional
    public ResponseEntity<?> registerBlackListContent(UserDetailsImpl userDetailsImpl, List<BlackListContentReq> blackListContentReqList) {
        for (BlackListContentReq blackListContentReq : blackListContentReqList) {
            BlackList blackList = validBlackListById(blackListContentReq.getBlacklistId());
            blackList.updateContent(blackListContentReq.getContent());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("사유가 작성되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 블랙리스트 삭제
    @Transactional
    public ResponseEntity<?> deleteBlackList(UserDetailsImpl userDetailsImpl, Long blackListId) {
        BlackList blackList = validBlackListById(blackListId);
        User user = blackList.getStudent().getUser();
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        if (!adminUser.getSchool().equals(user.getSchool()))
            throw new InvalidSchoolAuthorityException();

        // 블랙리스트 데이터 삭제
        blackListRepository.delete(blackList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("블랙리스트가 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private BlackList validBlackListById(Long blackListId) {
        Optional<BlackList> findBlackList = blackListRepository.findById(blackListId);
        DefaultAssert.isTrue(findBlackList.isPresent(), "블랙리스트 정보가 올바르지 않습니다.");
        return findBlackList.get();
    }

    private User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }
}
