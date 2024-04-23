package dormease.dormeasedev.domain.user.service;

import dormease.dormeasedev.domain.blacklist.domain.BlackList;
import dormease.dormeasedev.domain.blacklist.domain.repository.BlackListRepository;
import dormease.dormeasedev.domain.blacklist.dto.request.BlackListContentReq;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.UserType;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.dto.response.BlackListUserInfoRes;
import dormease.dormeasedev.domain.user.dto.response.DeleteUserInfoRes;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BlackListService {

    private final UserRepository userRepository;
    private final BlackListRepository blackListRepository;

    // 블랙리스트 목록 조회
    public ResponseEntity<?> getBlackListUsers(CustomUserDetails customUserDetails, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        // 목록 조회 및 페이징
        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<User> blackListedUsersPage = userRepository.findBySchoolAndUserType(admin.getSchool(), UserType.BLACKLIST, pageable);

        List<BlackListUserInfoRes> blackListUserInfoRes = blackListedUsersPage.getContent().stream()
                .map(user -> {
                    BlackList blackList = blackListRepository.findByUser(user);
                    return BlackListUserInfoRes.builder()
                            .id(blackList.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .minusPoint(user.getMinusPoint())
                            .content(blackList.getContent())
                            .createdAt(blackList.getCreatedDate().toLocalDate())
                            .build();
                })
                .collect(Collectors.toList());

        // ApiResponse 객체 생성 및 반환
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(blackListUserInfoRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 블랙리스트 사유 작성
    @Transactional
    public ResponseEntity<?> registerBlackListContent(CustomUserDetails customUserDetails, Long blackListId, BlackListContentReq blackListContentReq) {
        BlackList blackList = validBlackListById(blackListId);
        blackList.updateContent(blackListContentReq.getContent());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("사유가 작성되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 블랙리스트 삭제
    @Transactional
    public ResponseEntity<?> deleteBlackList(CustomUserDetails customUserDetails, Long blackListId) {
        BlackList blackList = validBlackListById(blackListId);
        User user = blackList.getUser();
        // user 블랙리스트 해제
        user.updateUserType(UserType.USER);
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
