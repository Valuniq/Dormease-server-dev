package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.UpdateDormitoryNameReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.DormitorySettingListRes;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import dormease.dormeasedev.infrastructure.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitorySettingService {

    private final DormitoryRepository dormitoryRepository;
    private final ResidentRepository residentRepository;
    private final RoomRepository roomRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final UserService userService;

    private final S3Uploader s3Uploader;

    // [건물 설정] 건물 추가
    @Transactional
    public ResponseEntity<?> registerDormitory(UserDetailsImpl userDetailsImpl) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        // 건물 개수 검증
        checkDormitoryLimit(admin);

        String dormitoryName = generateAvailableDormitoryName(admin);
        Dormitory dormitory = Dormitory.builder()
                    .school(admin.getSchool())
                    .name(dormitoryName)
                    .roomCount(0)
                    .imageUrl(null)
                    .build();
        dormitoryRepository.save(dormitory);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(
                        Message.builder()
                                .message("건물이 생성되었습니다.")
                                .build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private String generateAvailableDormitoryName(User user) {
        String name = "건물명";
        int num = 1;
        String dormitoryName = name;
        while (dormitoryRepository.existsBySchoolAndName(user.getSchool(), dormitoryName)) {
            dormitoryName = name + num;
            num++;
        }
        return dormitoryName;
    }

    private void checkDormitoryLimit(User user) {
        // 하나의 학교당 기숙사 개수 최대 15개 제한
        int uniqueDormitoryCount = dormitoryRepository.countBySchool(user.getSchool());
        DefaultAssert.isTrue(uniqueDormitoryCount < 15, "생성할 수 있는 최대 개수는 15개입니다.");
    }

    // [건물 설정] 건물 목록 조회
    public ResponseEntity<?> getDormitoriesBySchool(UserDetailsImpl userDetailsImpl) {
        User user = userService.validateUserById(userDetailsImpl.getUserId());
        // 학교별 건물 조회
        List<Dormitory> dormitories = dormitoryRepository.findBySchoolOrderByCreatedDateAsc(user.getSchool());
        List<DormitorySettingListRes> dormitorySettingListRes = dormitories.stream()
                .map(dormitory -> {
                    // DormitoryRoomType 및 관련된 DormitoryTerm 조회
                    List<DormitoryRoomType> dormitoryRoomTypes = dormitoryRoomTypeRepository.findByDormitory(dormitory);
                    List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByDormitoryRoomTypeIn(dormitoryRoomTypes);
                    return DormitorySettingListRes.builder()
                            .id(dormitory.getId())
                            .name(dormitory.getName())
                            .imageUrl(dormitory.getImageUrl())
                            .assignedResidents(hasRelatedResidents(dormitoryTermList))
                            .build();
                })
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitorySettingListRes)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    // [건물 설정] 건물 사진 추가 및 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryImage(UserDetailsImpl userDetailsImpl, Long dormitoryId, MultipartFile image) throws IOException {
        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Dormitory dormitory = validDormitoryById(dormitoryId);

        // 기존 이미지 삭제
        String originalFilePath = dormitory.getImageUrl();
        if (originalFilePath != null && originalFilePath.contains("amazonaws.com/")) {
            String originalFileName = originalFilePath.split("amazonaws.com/")[1];
            s3Uploader.deleteFile(originalFileName);
        }

        // s3 이미지 업로드
        String imagePath = s3Uploader.uploadImage(image);

        dormitory.updateImageUrl(imagePath);
        // dormitoryRepository.save(dormitory);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("사진이 변경되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    // 건물 삭제
    // (해당 건물에 배정된 사생이 있을 시, 입사신청설정에 사용된 경우 삭제 불가)
    @Transactional
    public ResponseEntity<?> deleteDormitory(UserDetailsImpl userDetailsImpl, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        // DormitoryRoomType 및 관련된 DormitoryTerm 조회
        List<DormitoryRoomType> dormitoryRoomTypes = dormitoryRoomTypeRepository.findByDormitory(dormitory);
        List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByDormitoryRoomTypeIn(dormitoryRoomTypes);

        // 관련된 사생 데이터 확인
        DefaultAssert.isTrue(!hasRelatedResidents(dormitoryTermList), "해당 건물에 배정된 사생이 있어 삭제할 수 없습니다.");

        // 호실 삭제
        roomRepository.deleteByDormitory(dormitory);

        // 입사신청설정에 사용된 적이 있는지 확인
        boolean isDeletable = !dormitoryTermRepository.existsByDormitoryRoomTypeIn(dormitoryRoomTypes);
        if (isDeletable) {
            if (dormitory.getImageUrl() != null) {
                s3Uploader.deleteFile(dormitory.getImageUrl());
            }
            // 기숙사와 연결된 DormitoryRoomType 삭제
            dormitoryRoomTypeRepository.deleteAll(dormitoryRoomTypes);
            dormitoryRepository.delete(dormitory); // 기숙사 삭제
        } else {
            // 소프트 삭제 처리
            dormitory.updateStatus(Status.DELETE);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("건물이 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    private boolean hasRelatedResidents(List<DormitoryTerm> dormitoryTerms) {
        return residentRepository.existsByDormitoryTermIn(dormitoryTerms);
    }

    // 건물명 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryName(UserDetailsImpl userDetailsImpl, Long dormitoryId, UpdateDormitoryNameReq updateDormitoryNameReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 이미 존재하는 이름이면 변경 불가
        boolean availableName = !dormitoryRepository.existsBySchoolAndName(dormitory.getSchool(), updateDormitoryNameReq.getName());

        String msg = "건물명이 변경되었습니다.";
        boolean check = true;
        if (availableName) {
            // 기숙사 이름 변경
            dormitory.updateName(updateDormitoryNameReq.getName());
        } else {
            msg = "중복된 건물명이 존재합니다.";
            check = false;
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(check)
                .information(Message.builder().message(msg).build())  // 조회 메소드 호출
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }


}
