package dormease.dormeasedev.domain.point.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.point.domain.Point;
import dormease.dormeasedev.domain.point.domain.PointType;
import dormease.dormeasedev.domain.point.domain.repository.PointRepository;
import dormease.dormeasedev.domain.point.dto.request.*;
import dormease.dormeasedev.domain.point.dto.response.PointRes;
import dormease.dormeasedev.domain.point.dto.response.TotalUserPointRes;
import dormease.dormeasedev.domain.point.dto.response.ResidentInfoRes;
import dormease.dormeasedev.domain.point.dto.response.UserPointDetailRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.user_point.domain.UserPoint;
import dormease.dormeasedev.domain.user_point.domain.repository.UserPointRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PointService {

    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;
    private final PointRepository pointRepository;
    private final UserPointRepository userPointRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;


    // 상벌점 리스트 내역 조회
    public ResponseEntity<?> getPointList(CustomUserDetails customUserDetails) {
        User admin = validUserById(customUserDetails.getId());

        List<Point> points = pointRepository.findBySchoolAndStatus(admin.getSchool(), Status.ACTIVE);
        List<PointRes> pointResList = points.stream()
                .map(point -> PointRes.builder()
                        .pointId(point.getId())
                        .content(point.getContent())
                        .score(point.getScore())
                        .pointType(point.getPointType())
                        .build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pointResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 상벌점 리스트 내역 등록
    // TODO: 수정 필요, req 값에 id 추가 후 받은 id와 findById 해서 안 맞으면 새로 등록할 것.
    @Transactional
    public ResponseEntity<?> registerPointList(CustomUserDetails customUserDetails, PointListReq pointListReqs) {
        User admin = validUserById(customUserDetails.getId());
        List<BonusPointManagementReq> bonusPointList = pointListReqs.getBonusPointList();
        List<MinusPointManagementReq> minusPointList = pointListReqs.getMinusPointList();

        if(!bonusPointList.isEmpty()) {
            List<Point> bonusPoint = bonusPointList.stream()
                    .map(bonusPointManagementReq -> Point.builder()
                            .school(admin.getSchool())
                            .pointType(PointType.BONUS)
                            .content(bonusPointManagementReq.getContent())
                            .score(bonusPointManagementReq.getScore())
                            .build())
                    .collect(Collectors.toList());
            pointRepository.saveAll(bonusPoint);
        }
        if(!minusPointList.isEmpty()) {
            List<Point> minusPoint = minusPointList.stream()
                    .map(minusPointManagementReq -> Point.builder()
                            .school(admin.getSchool())
                            .pointType(PointType.MINUS)
                            .content(minusPointManagementReq.getContent())
                            .score(minusPointManagementReq.getScore())
                            .build())
                    .collect(Collectors.toList());
            pointRepository.saveAll(minusPoint);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("상벌점 내역이 등록되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 상벌점 리스트 내역 삭제
    @Transactional
    public ResponseEntity<?> deletePoint(CustomUserDetails customUserDetails, Long pointId) {
        User admin = validUserById(customUserDetails.getId());
        Point point = validPointById(pointId);

        // 삭제해도 사용자의 상벌점 내역 조회 가능, 따라서 회원 상벌점 내역에 해당 내역이 있다면 soft delete
        // 아니라면 hard delete
        List<UserPoint> userPoints = userPointRepository.findByPoint(point);
        if (userPoints.isEmpty()) {
            pointRepository.delete(point);
        } else { point.updateStatus(Status.DELETE); }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("상벌점 내역이 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    // Description : 상벌점 내역 중 사생 관련 기능

    // 상벌점 부여
    // TODO: type 안 받게 수정
    @Transactional
    public ResponseEntity<?> addUserPoints(CustomUserDetails customUserDetails, Long residentId, List<AddPointToResidentReq> addPointToResidentReqs, String pointType) {
        User admin = validUserById(customUserDetails.getId());
        Resident resident = validResidentById(residentId);

        User user = resident.getUser();
        PointType type = PointType.valueOf(pointType.toUpperCase());

        List<UserPoint> userPoints = addPointToResidentReqs.stream()
                .map(req -> {
                    Point point = validPointById(req.getPointId());
                    DefaultAssert.isTrue(point.getPointType() == type, "내역과 상벌점 타입이 일치하지 않습니다.");
                    DefaultAssert.isTrue(point.getStatus() == Status.ACTIVE, "삭제된 상벌점 내역은 부여할 수 없습니다.");
                    return UserPoint.builder()
                            .user(user)
                            .point(validPointById(req.getPointId()))
                            .build();
                })
                .collect(Collectors.toList());

        userPointRepository.saveAll(userPoints);
        updatePoint(user, type);

        String message = type == PointType.BONUS ? "상점" : "벌점";
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message(message + "이 부여되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void updatePoint(User user, PointType pointType) {
        String message = pointType == PointType.BONUS ? "상점" : "벌점";

        List<UserPoint> userPoints = userPointRepository.findByUser(user);
        DefaultAssert.isTrue(!userPoints.isEmpty(), message + "이 부여되지 않았습니다.");

        Integer totalPoint = userPoints.stream()
                .filter(userPoint -> userPoint.getPoint().getPointType().equals(pointType))
                .mapToInt(userPoint -> userPoint.getPoint().getScore())
                .sum();

        if (pointType == PointType.BONUS) { user.updateBonusPoint(totalPoint);
        } else { user.updateMinusPoint(totalPoint); }
    }

    // 상벌점 내역 삭제
    @Transactional
    public  ResponseEntity<?> deleteUserPoints(CustomUserDetails customUserDetails, Long residentId, List<DeleteUserPointReq> deleteUserPointReqs) {
        User admin = validUserById(customUserDetails.getId());
        Resident resident = validResidentById(residentId);

        User user = resident.getUser();
        List<UserPoint> userPoints = deleteUserPointReqs.stream()
                .map(DeleteUserPointReq::getUserPointId)
                .map(this::validUserPointById)
                .collect(Collectors.toList());

        userPointRepository.deleteAll(userPoints);

        updatePoint(user, PointType.BONUS);
        updatePoint(user, PointType.MINUS);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("내역이 삭제되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 상벌점 내역 조회
    public ResponseEntity<?> getUserPoints(CustomUserDetails customUserDetails, Long residentId, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        Resident resident = validResidentById(residentId);

        User user = resident.getUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        // 사생 목록 조회 (페이징 적용)
        Page<UserPoint> userPoints = userPointRepository.findUserPointsByUser(user, pageable);
        List<UserPointDetailRes> userPointDetailRes = userPoints.stream()
                .map(userPoint -> UserPointDetailRes.builder()
                        .userPointId(userPoint.getId())
                        .content(userPoint.getPoint().getContent())
                        .score(userPoint.getPoint().getScore())
                        .pointType(userPoint.getPoint().getPointType())
                        .createdAt(userPoint.getCreatedDate().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, userPoints);

        TotalUserPointRes totalUserPointRes = TotalUserPointRes.builder()
                .pageInfo(pageInfo)
                .userPointDetailRes(userPointDetailRes)
                .bonusPoint(user.getBonusPoint())
                .minusPoint(user.getMinusPoint())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(totalUserPointRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<ResidentInfoRes> userResidentInfoResList = residents.getContent().stream()
                .map(resident -> {
                    // null 여부 확인
                    String dormitoryName = getDormitoryName(resident);
                    Integer roomNumber = getRoomNumber(resident);
                    return ResidentInfoRes.builder()
                            .id(resident.getId())
                            .name(resident.getUser().getName())
                            .studentNumber(resident.getUser().getStudentNumber())
                            .phoneNumber(resident.getUser().getPhoneNumber())
                            .bonusPoint(resident.getUser().getBonusPoint())
                            .minusPoint(resident.getUser().getMinusPoint())
                            // 사생의 호실 미배정 고려, dormitory: 사생-회원-입사신청-거주기간-기숙사로 찾기
                            // 해당 탐색 과정에서 null 값이 하나라도 있을 경우 null return
                            .dormitory(dormitoryName)
                            // 사생의 호실 미배정 고려 null 허용
                            .room(roomNumber)
                            .build();
                })
                .sorted(Comparator.comparing(ResidentInfoRes::getName))
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, residents);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, userResidentInfoResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private String getDormitoryName(Resident resident) {
        // 호실 배정 시
        if (resident.getRoom() != null) {
            Dormitory dormitory = resident.getRoom().getDormitory();
            return formatDormitoryName(dormitory);
        // 호실 미배정 시
        } else {
            Dormitory dormitory = findDormitoryByResident(resident);
            if (dormitory != null) {
                return formatDormitoryName(dormitory);
            }
        }
        return null;
    }

    private Integer getRoomNumber(Resident resident) {
        if (resident.getRoom() != null) {
            return resident.getRoom().getRoomNumber();
        } else {
            return null;
        }
    }

    private String formatDormitoryName(Dormitory dormitory) {
        return dormitory.getName() + "(" + dormitory.getRoomSize() + "인실)";
    }

    private Dormitory findDormitoryByResident(Resident resident) {
        List<DormitoryApplicationResult> validResults = Arrays.asList(DormitoryApplicationResult.PASS, DormitoryApplicationResult.MOVE_PASS);

        return dormitoryApplicationRepository.findTop1ByUserAndResultsOrderByCreatedDateDesc(resident.getUser(), validResults)
                .map(DormitoryApplication::getDormitoryTerm)
                .map(DormitoryTerm::getDormitory)
                .orElse(null);
    }

    public ResponseEntity<?> getSortedResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);
        List<ResidentInfoRes> residentInfoResList = residents.getContent().stream()
                .map(resident -> {
                    // null 여부 확인
                    String dormitoryName = getDormitoryName(resident);
                    Integer roomNumber = getRoomNumber(resident);
                    return ResidentInfoRes.builder()
                            .id(resident.getId())
                            .name(resident.getUser().getName())
                            .studentNumber(resident.getUser().getStudentNumber())
                            .phoneNumber(resident.getUser().getPhoneNumber())
                            .bonusPoint(resident.getUser().getBonusPoint())
                            .minusPoint(resident.getUser().getMinusPoint())
                            // 사생의 호실 미배정 고려, room으로 찾는 게 아니라 입사신청 설정으로 찾기
                            .dormitory(dormitoryName)
                            // 사생의 호실 미배정 고려 null 허용
                            .room(roomNumber)
                            .build();
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, residents);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, residentInfoResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;

        Pageable pageable = PageRequest.of(page, 25);
        // 사생 목록 조회 (페이징 적용)
        Page<Resident> residents = residentRepository.searchResidentsByKeyword(admin.getSchool(), cleanedKeyword, pageable);
        List<ResidentInfoRes> userResidentInfoResList = residents.getContent().stream()
                .map(resident -> {
                    // null 여부 확인
                    String dormitoryName = getDormitoryName(resident);
                    Integer roomNumber = getRoomNumber(resident);
                    return ResidentInfoRes.builder()
                            .id(resident.getId())
                            .name(resident.getUser().getName())
                            .studentNumber(resident.getUser().getStudentNumber())
                            .phoneNumber(resident.getUser().getPhoneNumber())
                            .bonusPoint(resident.getUser().getBonusPoint())
                            .minusPoint(resident.getUser().getMinusPoint())
                            // 사생의 호실 미배정 고려, room으로 찾는 게 아니라 입사신청 설정으로 찾기
                            .dormitory(dormitoryName)
                            // 사생의 호실 미배정 고려 null 허용
                            .room(roomNumber)
                            .build();
                })
                .sorted(Comparator.comparing(ResidentInfoRes::getName))
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, residents);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, userResidentInfoResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private User validUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

    private Resident validResidentById(Long residentId) {
        Optional<Resident> resident = residentRepository.findById(residentId);
        DefaultAssert.isTrue(resident.isPresent(), "사생 정보가 올바르지 않습니다.");
        return resident.get();
    }

    private Point validPointById(Long pointId) {
        Optional<Point> point = pointRepository.findById(pointId);
        DefaultAssert.isTrue(point.isPresent(), "상벌점 정보가 올바르지 않습니다.");
        return point.get();
    }

    private UserPoint validUserPointById(Long userPointId) {
        Optional<UserPoint> userPoint = userPointRepository.findById(userPointId);
        DefaultAssert.isTrue(userPoint .isPresent(), "유저의 상벌점 정보가 올바르지 않습니다.");
        return userPoint.get();
    }
}
