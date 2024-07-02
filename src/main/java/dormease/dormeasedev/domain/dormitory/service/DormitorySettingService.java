package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.RegisterDormitoryReq;
import dormease.dormeasedev.domain.dormitory.dto.request.UpdateDormitoryNameReq;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitorySettingListRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.s3.service.S3Uploader;
import dormease.dormeasedev.domain.user.domain.Gender;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitorySettingService {

    private final DormitoryRepository dormitoryRepository;
    private final ResidentRepository residentRepository;
    private final UserService userService;

    private final S3Uploader s3Uploader;

    // [건물 설정] 건물 추가
    @Transactional
    public ResponseEntity<?> registerDormitory(CustomUserDetails customUserDetails, RegisterDormitoryReq registerDormitoryReq,
                                               MultipartFile image) {
        User user = userService.validateUserById(customUserDetails.getId());
        boolean isEmptySameNameDormitories = dormitoryRepository.findBySchoolAndName(user.getSchool(), registerDormitoryReq.getName()).isEmpty();

        boolean check = true;
        String msg = "건물이 추가되었습니다.";
        if (isEmptySameNameDormitories) {
            Dormitory dormitory = Dormitory.builder()
                    .school(user.getSchool())
                    .name(registerDormitoryReq.getName())
                    .gender(Gender.EMPTY)
                    .roomCount(0)
                    .imageUrl(setAWSImage(image))
                    .build();

            dormitoryRepository.save(dormitory);
        } else {
            check = false;
            msg = "동일한 이름의 기숙사가 존재합니다.";
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(check)
                .information(Message.builder().message(msg).build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    // 이미지 추가 메소드 구현
    private String setAWSImage(MultipartFile image) {
        if (image.isEmpty()) {
            return null;
        } else
            return s3Uploader.uploadImage(image);
    }


    // [건물 설정] 건물 목록 조회
    public ResponseEntity<?> getDormitoriesBySchool(CustomUserDetails customUserDetails) {
        User user = userService.validateUserById(customUserDetails.getId());

        // 학교별 건물 조회
        List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());

        // 기존에 등록된 기숙사 이름 저장
        Set<String> existingDormitoryNames = ConcurrentHashMap.newKeySet();

        List<DormitorySettingListRes> dormitorySettingListRes;

        // 기존에 등록된 기숙사 이름 추가
        synchronized (existingDormitoryNames) {
            dormitorySettingListRes = dormitories.stream()
                    .filter(dormitory -> existingDormitoryNames.add(dormitory.getName()))
                    .map(dormitory -> DormitorySettingListRes.builder()
                            .id(dormitory.getId())
                            .name(dormitory.getName())
                            .imageUrl(dormitory.getImageUrl())
                            .assignedResidents(hasRelatedResidents(dormitory))
                            .build())
                    .collect(Collectors.toList());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitorySettingListRes)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    // [건물 설정] 건물 사진 추가 및 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryImage(CustomUserDetails customUserDetails, Long dormitoryId, MultipartFile image) {
        User user = userService.validateUserById(customUserDetails.getId());
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

        // 동일 학교와 동일 건물 이름을 가진 모든 건물의 이미지 경로 업데이트
        dormitoryRepository.updateImageUrlForSchoolAndDorm(user.getSchool(), dormitory.getName(), imagePath);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("사진이 변경되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    // 건물 삭제(해당 건물에 배정된 사생이 있을 시 삭제 불가)
    // 연결된 room도 다 삭제하기
    @Transactional
    public ResponseEntity<?> deleteDormitory(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());

        // 관련된 사생 데이터 확인
        DefaultAssert.isTrue(!hasRelatedResidents(sameNameDormitories), "해당 건물에 배정된 사생이 있어 삭제할 수 없습니다.");

        // 건물 이미지 삭제
        if (dormitory.getImageUrl() != null) {
            s3Uploader.deleteFile(dormitory.getImageUrl());
        }
        // 건물 삭제
        dormitoryRepository.deleteAll(sameNameDormitories);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("건물이 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    //private boolean hasRelatedResidents(List<Dormitory> sameNameDormitories) {
    //    List<Resident> residents = new ArrayList<>();
    //    for (Dormitory dormitory : sameNameDormitories) {
    //        List<Room> rooms = roomRepository.findByDormitory(dormitory);
    //        for (Room room : rooms) {
    //            List<Resident> findResidents = residentRepository.findByRoom(room);
    //            residents.addAll(findResidents);
    //        }
    //    }
    //    return !residents.isEmpty();
    //}

    private boolean hasRelatedResidents(List<Dormitory> sameNameDormitories) {
        List<Resident> residents = residentRepository.findByDormitories(sameNameDormitories);
        return !residents.isEmpty();
    }

    private boolean hasRelatedResidents(Dormitory dormitory) {
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        List<Resident> residents = residentRepository.findByDormitories(sameNameDormitories);
        return !residents.isEmpty();
    }

    // 건물명 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryName(CustomUserDetails customUserDetails, Long dormitoryId, UpdateDormitoryNameReq updateDormitoryNameReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        // 이미 존재하는 이름이면 변경 불가
        DefaultAssert.isTrue(dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), updateDormitoryNameReq.getName()).isEmpty(), "해당 이름의 건물이 이미 존재합니다.");

        // 동일 기숙사 조회
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");
        // 기숙사 이름 일괄 변경
        dormitoryRepository.updateNamesBySchoolAndName(dormitory.getSchool(), dormitory.getName(), updateDormitoryNameReq.getName());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("건물명이 변경되었습니다.").build())  // 조회 메소드 호출
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }


}
