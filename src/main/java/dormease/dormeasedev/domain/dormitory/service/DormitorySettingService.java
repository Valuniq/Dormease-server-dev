package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.RegisterDormitoryReq;
import dormease.dormeasedev.domain.dormitory.dto.request.UpdateDormitoryNameReq;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryRes;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.s3.service.S3Uploader;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.Gender;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.error.DefaultException;
import dormease.dormeasedev.global.error.DefaultNullPointerException;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.ErrorCode;
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
    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;

    private final S3Uploader s3Uploader;

    // [건물 설정] 건물 추가
    @Transactional
    public ResponseEntity<?> registerDormitory(CustomUserDetails customUserDetails, RegisterDormitoryReq registerDormitoryReq,
                                               MultipartFile image) {
        User user = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

        Dormitory dormitory = Dormitory.builder()
                .school(user.getSchool())
                .name(registerDormitoryReq.getName())
                .gender(Gender.EMPTY)
                .roomCount(0)
                .imageUrl(setAWSImage(image))
                .build();

        dormitoryRepository.save(dormitory);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("건물이 추가되었습니다").build();

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
    public ResponseEntity<?> getDormitoriesExceptGenderBySchool(CustomUserDetails customUserDetails) {
        try {
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

            // 학교별 건물 조회
            List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());

            // 기존에 등록된 기숙사 이름 저장
            Set<String> existingDormitoryNames = ConcurrentHashMap.newKeySet();

            List<DormitoryRes> dormitoryResList;

            // 기존에 등록된 기숙사 이름 추가
            synchronized (existingDormitoryNames) {
                dormitoryResList = dormitories.stream()
                        .filter(dormitory -> existingDormitoryNames.add(dormitory.getName()))
                        .map(dormitory -> DormitoryRes.builder()
                                .id(dormitory.getId())
                                .name(dormitory.getName())
                                .imageUrl(dormitory.getImageUrl())
                                .build())
                        .collect(Collectors.toList());
            }

            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information(dormitoryResList)
                    .build();

            return ResponseEntity.ok(apiResponse);

        } catch (DefaultNullPointerException e) {
            throw new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER);
        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 건물 상세 조회


    // [건물 설정] 건물 사진 추가 및 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryImage(CustomUserDetails customUserDetails, Long dormitoryId, MultipartFile image) {
        try {
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

            Dormitory dormitory = dormitoryRepository.findById(dormitoryId)
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "건물이 존재하지 않습니다."));

            // 학교가 동일한지 확인
            isSameSchoolAsDormitory(user.getSchool(), dormitory.getSchool());

            // s3 이미지 업로드
            String imagePath = s3Uploader.uploadImage(image);

            dormitory.updateImageUrl(imagePath);
            dormitoryRepository.save(dormitory);

            // 동일 학교와 동일 건물 이름을 가진 모든 건물의 이미지 경로 업데이트
            dormitoryRepository.updateImageUrlForSchoolAndDorm(user.getSchool(), dormitory.getName(), imagePath);

            // 기존 이미지 삭제
            String originalFilePath = dormitory.getImageUrl();
            if (originalFilePath != null && originalFilePath.contains("amazonaws.com/")) {
                String originalFileName = originalFilePath.split("amazonaws.com/")[1];
                s3Uploader.deleteFile(originalFileName);
            }

            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information("사진이 변경되었습니다.")
                    .build();
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR, "사진 추가 중 오류가 발생하였습니다.");
        }
    }

    // 건물 삭제(해당 건물에 배정된 사생이 있을 시 삭제 불가)
    @Transactional
    public ResponseEntity<?> deleteDormitory(CustomUserDetails customUserDetails, Long dormitoryId) {
        try {
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

            Dormitory dormitory = dormitoryRepository.findById(dormitoryId)
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "건물이 존재하지 않습니다."));

            isSameSchoolAsDormitory(user.getSchool(), dormitory.getSchool());

            List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());

            // 관련된 사생 데이터 확인
            if (hasRelatedResidents(sameNameDormitories)) {
                throw new DefaultException(ErrorCode.INVALID_CHECK, "해당 건물에 배정된 사생이 있어 삭제할 수 없습니다.");
            }

            // 건물 이미지 삭제
            if (dormitory.getImageUrl() != null) {
                s3Uploader.deleteFile(dormitory.getImageUrl());
            }

            // 건물 삭제
            dormitoryRepository.deleteAll(sameNameDormitories);

            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information("건물이 삭제되었습니다.")
                    .build();
            return ResponseEntity.ok(apiResponse);

        } catch (DefaultException e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR, "건물 삭제 중 오류가 발생하였습니다.");
        }
    }

    private boolean hasRelatedResidents(List<Dormitory> sameNameDormitories) {
        for (Dormitory dormitory : sameNameDormitories) {
                // DormitorySettingTerm을 통해 Resident 조회
                List<DormitorySettingTerm> settingTerms = dormitorySettingTermRepository.findByDormitory(dormitory);
                for (DormitorySettingTerm settingTerm : settingTerms) {
                    List<Resident> residents = residentRepository.findByDormitorySettingTerm(settingTerm);
                    if (!residents.isEmpty()) {
                        return true; // 관련된 Resident 데이터가 존재하는 경우
                    }
                }
        }
        return false; // 관련된 Resident 데이터가 없는 경우
    }

    private void isSameSchoolAsDormitory(School userSchool, School dormitorySchool) {
        if (!Objects.equals(userSchool, dormitorySchool)) {
            throw new DefaultException(ErrorCode.INVALID_CHECK, "학교가 일치하지 않습니다");
        }
    }

    // 건물명 변경
    @Transactional
    public ResponseEntity<?> updateDormitoryName(CustomUserDetails customUserDetails, Long dormitoryId, UpdateDormitoryNameReq updateDormitoryNameReq) {
        try {
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

            Dormitory dormitory = dormitoryRepository.findById(dormitoryId)
                    .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "건물이 존재하지 않습니다."));

            isSameSchoolAsDormitory(user.getSchool(), dormitory.getSchool());

            // 이미 존재하는 이름이면 변경 불가
            if (dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), updateDormitoryNameReq.getName()).isEmpty()) {
                throw new DefaultException(ErrorCode.INVALID_PARAMETER, "해당 이름의 건물이 이미 존재합니다.");
            }

            // 기숙사 이름 일괄 변경
            List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());

            if (!sameNameDormitories.isEmpty()) {
                dormitoryRepository.updateNamesBySchoolAndName(dormitory.getSchool(), dormitory.getName(), updateDormitoryNameReq.getName());

                ApiResponse apiResponse = ApiResponse.builder()
                        .check(true)
                        .information("건물명이 변경되었습니다.")  // 조회 메소드 호출
                        .build();

                return ResponseEntity.ok(apiResponse);
            } else
                throw new DefaultException(ErrorCode.INVALID_OPTIONAL_ISPRESENT, "해당 건물명의 건물이 존재하지 않습니다.");

        } catch (DefaultException e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR, "건물명 변경 중 오류가 발생하였습니다.");
        }
    }

}
