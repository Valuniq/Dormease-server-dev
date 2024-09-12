package dormease.dormeasedev.domain.points.point.service;

import dormease.dormeasedev.domain.common.Status;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.points.point.domain.Point;
import dormease.dormeasedev.domain.points.point.domain.PointType;
import dormease.dormeasedev.domain.points.point.domain.repository.PointRepository;
import dormease.dormeasedev.domain.points.point.dto.request.*;
import dormease.dormeasedev.domain.points.point.dto.response.*;
import dormease.dormeasedev.domain.points.user_point.domain.UserPoint;
import dormease.dormeasedev.domain.points.user_point.domain.repository.UserPointRepository;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    // Description: [APP] 상벌점 관련 기능
    // 회원 상벌점 조회
    public ResponseEntity<?> getUserPointTotal(CustomUserDetails customUserDetails) {
        User user = validUserById(customUserDetails.getId());
        UserPointAppRes userPointAppRes = UserPointAppRes.builder()
                .bonusPoint(user.getBonusPoint())
                .minusPoint(user.getMinusPoint())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointAppRes)
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    // 회원 상점/벌점 내역 조회
    public ResponseEntity<?> getUserPointHistory(CustomUserDetails customUserDetails, String type) {
        User user = validUserById(customUserDetails.getId());
        List<UserPoint> userPoints = userPointRepository.findUserPointsByUserAndPoint_pointTypeOrderByCreatedDateDesc(user, PointType.valueOf(type));

        List<UserPointHistoryAppRes> userPointHistoryAppResList = userPoints.stream()
                .map(userPoint -> UserPointHistoryAppRes.builder()
                        .userPointId(userPoint.getId())
                        .createdDate(userPoint.getCreatedDate().toLocalDate())
                        .content(userPoint.getPoint().getContent())
                        .score(userPoint.getPoint().getScore())
                        .build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointHistoryAppResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    // Description: [WEB] 상벌점 관련 기능
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
    @Transactional
    public ResponseEntity<?> registerPointList(CustomUserDetails customUserDetails, PointListReq pointListReqs) {
        User admin = validUserById(customUserDetails.getId());
        School school = admin.getSchool();

        List<BonusPointManagementReq> bonusPointList = pointListReqs.getBonusPointList();
        List<MinusPointManagementReq> minusPointList = pointListReqs.getMinusPointList();
        List<Point> pointListToSave = new ArrayList<>();
        for (BonusPointManagementReq bonusPointManagementReq : bonusPointList) {
            Point bonusPoint = Point.builder()
                    .school(school)
                    .pointType(PointType.BONUS)
                    .score(bonusPointManagementReq.getScore())
                    .content(bonusPointManagementReq.getContent())
                    .build();
            pointListToSave.add(bonusPoint);
        }
        for (MinusPointManagementReq minusPointManagementReq : minusPointList) {
            Point minusPoint = Point.builder()
                    .school(school)
                    .pointType(PointType.MINUS)
                    .score(minusPointManagementReq.getScore())
                    .content(minusPointManagementReq.getContent())
                    .build();
            pointListToSave.add(minusPoint);
        }
        pointRepository.saveAll(pointListToSave);

        //
//        if (!bonusPointList.isEmpty()) {
//            List<Point> bonusPointsToSave = bonusPointList.stream()
//                    // 요청받은 상점 내역의 존재 여부 확인
//                    .filter(bonusPointManagementReq -> !pointRepository.existsByIdAndScoreAndPointType(
//                            bonusPointManagementReq.getPointId(),
//                            bonusPointManagementReq.getScore(),
//                            PointType.BONUS
//                    ))
//                    // 해당하는 상점 내역이 없을 경우 리스트에 추가
//                    .map(bonusPointManagementReq -> Point.builder()
//                            .school(admin.getSchool())
//                            .pointType(PointType.BONUS)
//                            .content(bonusPointManagementReq.getContent())
//                            .score(bonusPointManagementReq.getScore())
//                            .build())
//                    .collect(Collectors.toList());
//
//            if (!bonusPointsToSave.isEmpty()) {
//                pointRepository.saveAll(bonusPointsToSave);
//            }
//        }
//        if (!minusPointList.isEmpty()) {
//            List<Point> minusPointsToSave = minusPointList.stream()
//                    .filter(minusPointManagementReq -> !pointRepository.existsByIdAndScoreAndPointType(
//                            minusPointManagementReq.getPointId(),
//                            minusPointManagementReq.getScore(),
//                            PointType.MINUS
//                    ))
//                    .map(minusPointManagementReq -> Point.builder()
//                            .school(admin.getSchool())
//                            .pointType(PointType.MINUS)
//                            .content(minusPointManagementReq.getContent())
//                            .score(minusPointManagementReq.getScore())
//                            .build())
//                    .collect(Collectors.toList());
//
//            if (!minusPointsToSave.isEmpty()) {
//                pointRepository.saveAll(minusPointsToSave);
//            }
//        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(
                        Message.builder()
                                .message("상벌점 내역이 등록되었습니다.")
                                .build()
                )
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
    @Transactional
    public ResponseEntity<?> addUserPoints(CustomUserDetails customUserDetails, Long residentId, List<AddPointToResidentReq> addPointToResidentReqs) {
        User admin = validUserById(customUserDetails.getId());
        Resident resident = validResidentById(residentId);

        User user = resident.getUser();
        int bonus = 0;
        int minus = 0;

        for (AddPointToResidentReq addPointToResidentReq : addPointToResidentReqs) {
            Point point = validPointById(addPointToResidentReq.getPointId());
            DefaultAssert.isTrue(point.getStatus().equals(Status.ACTIVE), "삭제된 상벌점 내역은 부여할 수 없습니다.");

            UserPoint userPoint = UserPoint.builder()
                    .user(user)
                    .point(point)
                    .build();
            userPointRepository.save(userPoint);
            if (point.getPointType().equals(PointType.BONUS))
                bonus += point.getScore();
            else
                minus += point.getScore();
        }

        user.updateBonusPoint(user.getBonusPoint() + bonus);
        user.updateMinusPoint(user.getMinusPoint() + minus);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(
                        Message.builder()
                                .message("상/벌점이 부여되었습니다.")
                                .build()
                )
                .build();
        return ResponseEntity.ok(apiResponse);

        //
//        Set<PointType> pointTypes = new HashSet<>();
//        List<UserPoint> userPoints = addPointToResidentReqs.stream()
//                .map(req -> {
//                    Point point = validPointById(req.getPointId());
//                    DefaultAssert.isTrue(point.getStatus() == Status.ACTIVE, "삭제된 상벌점 내역은 부여할 수 없습니다.");
//                    pointTypes.add(point.getPointType());
//                    return UserPoint.builder()
//                            .user(user)
//                            .point(validPointById(req.getPointId()))
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        userPointRepository.saveAll(userPoints);
//        updatePoint(user, pointTypes);
//
//        ApiResponse apiResponse = ApiResponse.builder()
//                .check(true)
//                .information(Message.builder().message("상/벌점이 부여되었습니다.").build())
//                .build();
//        return ResponseEntity.ok(apiResponse);
    }

    private void updatePoint(User user, Set<PointType> pointTypes) {
        List<UserPoint> userPoints = userPointRepository.findByUser(user);
        DefaultAssert.isTrue(!userPoints.isEmpty(), "상/벌점이 부여되지 않았습니다.");

        for (PointType pointType : pointTypes) {
            Integer totalPoint = calculateTotalPoint(userPoints, pointType);
            if (pointType == PointType.BONUS) {
                user.updateBonusPoint(totalPoint);
            } else {
                user.updateMinusPoint(totalPoint);
            }
        }
    }

    private Integer calculateTotalPoint(List<UserPoint> userPoints, PointType pointType) {
        return userPoints.stream()
                .filter(userPoint -> userPoint.getPoint().getPointType().equals(pointType))
                .mapToInt(userPoint -> userPoint.getPoint().getScore())
                .sum();
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
        int bonus = 0;
        int minus = 0;
        for (UserPoint userPoint : userPoints) {
            Point point = userPoint.getPoint();
            PointType pointType = point.getPointType();
            if (pointType.equals(PointType.BONUS))
                bonus += point.getScore();
            else
                minus += point.getScore();
        }
        userPointRepository.deleteAll(userPoints);
        user.updateBonusPoint(user.getBonusPoint() + bonus);
        user.updateMinusPoint(user.getMinusPoint() + minus);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(
                        Message.builder()
                                .message("내역이 삭제되었습니다.")
                                .build()
                )
                .build();
        return ResponseEntity.ok(apiResponse);

        //
//        Set<PointType> pointTypes = userPoints.stream()
//                .map(userPoint -> userPoint.getPoint().getPointType())
//                .collect(Collectors.toSet());
//
//        userPointRepository.deleteAll(userPoints);
//        updatePoint(user, pointTypes);
//
//        ApiResponse apiResponse = ApiResponse.builder()
//                .check(true)
//                .information(Message.builder().message("내역이 삭제되었습니다.").build())
//                .build();
//        return ResponseEntity.ok(apiResponse);
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
                        .createdDate(userPoint.getCreatedDate().toLocalDate())
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

    // TODO : 확인 필요. 해당 메소드 필요 없다고 생각. 이유는 '미배정 사생은 직접 추가한 사생만 있고', 직접 추가한 사생은 상/벌점이 없기 때문.
    //  - '미배정 사생은 직접 추가한 사생만 있고' ==> 기획과도 이야기 하여 확정 필요
//    private String getDormitoryName(Resident resident) {
//        // 호실 배정 시
//        Room room = resident.getRoom();
//        if (room != null) {
//            Dormitory dormitory = room.getDormitory();
//            return dormitory.getName() + "(" + room.getRoomType().getRoomSize() + "인실)";
//        // 호실 미배정 시
//        } else {
//            Dormitory dormitory = findDormitoryByResident(resident);
//            if (dormitory != null) {
//                return dormitory.getName();
//            }
//        }
//        return null;
//    }

    //    private Dormitory findDormitoryByResident(Resident resident) {
//        User user = resident.getUser();
//        Optional<DormitoryApplication> findDormitoryApplication = dormitoryApplicationRepository.findByUserAndApplicationStatus(user, ApplicationStatus.NOW);
//        DefaultAssert.isTrue(findDormitoryApplication.isPresent(), "사생의 현재 입사 신청이 존재하지 않습니다.");
//        DormitoryApplication dormitoryApplication = findDormitoryApplication.get();
//        return dormitoryApplication.getDormitory();
//
////        return dormitoryApplicationRepository.findTop1ByUserAndResultsOrderByCreatedDateDesc(resident.getUser(), DormitoryApplicationResult.PASS)
////                .map(DormitoryApplication::getTerm)
////                .map(Term::getDormitory)
////                .orElse(null);
//    }

    private Integer getRoomNumber(Resident resident) {
        if (resident.getRoom() != null) {
            return resident.getRoom().getRoomNumber();
        } else {
            return null;
        }
    }

    // 전체 사생 대상 조회 및 정렬
    // 미회원 사생 배제 필요
    // Description: 기본 정렬 (sortBy: name, isAscending: true)
    public ResponseEntity<?> getResidents(CustomUserDetails customUserDetails, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);

        // userType이 RESIDENT인 user를 찾아서 사생 리스트 만들기
        List<User> users = userRepository.findBySchoolAndUserType(admin.getSchool(), UserType.RESIDENT);
        // user를 가지고 있는 resident 찾기 (페이징 적용)
        Page<Resident> residents = residentRepository.findResidentsByUsers(users, pageable);

        // 사생 목록 조회 (페이징 적용)
        // Page<Resident> residents = residentRepository.findByUserSchool(admin.getSchool(), pageable);

        List<ResidentInfoRes> residentInfoResList = residents.getContent().stream()
                .map(resident -> {
                    DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                    User user = resident.getUser();
                    return ResidentInfoRes.builder()
                            .id(resident.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .bonusPoint(user.getBonusPoint())
                            .minusPoint(user.getMinusPoint())
                            .dormitoryName(dormitoryRoomType.getDormitory().getName())
                            .roomSize(dormitoryRoomType.getRoomType().getRoomSize())
                            .room(getRoomNumber(resident))
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

    // 검색된 사생 대상 조회 및 정렬
    // Description: 기본 정렬 (sortBy: name, isAscending: true)
    public ResponseEntity<?> getSearchResidents(CustomUserDetails customUserDetails, String keyword, String sortBy, Boolean isAscending, Integer page) {
        User admin = validUserById(customUserDetails.getId());
        String cleanedKeyword = keyword.trim().toLowerCase();;
        String sortField = "user." + sortBy;
        Pageable pageable = PageRequest.of(page, 25, isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);

        /// userType이 RESIDENT인 user를 keyword로 검색 사생 리스트 만들기
        List<User> users = userRepository.searchUsersByKeyword(admin.getSchool(), cleanedKeyword, UserType.RESIDENT);
        // user를 가지고 있는 resident 찾기 (페이징 적용)
        Page<Resident> residents = residentRepository.findResidentsByUsers(users, pageable);

        List<ResidentInfoRes> userResidentInfoResList = residents.getContent().stream()
                .map(resident -> {
                    DormitoryRoomType dormitoryRoomType = resident.getDormitoryTerm().getDormitoryRoomType();
                    User user = resident.getUser();
                    return ResidentInfoRes.builder()
                            .id(resident.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .bonusPoint(user.getBonusPoint())
                            .minusPoint(user.getMinusPoint())
                            .dormitoryName(dormitoryRoomType.getDormitory().getName())
                            .roomSize(dormitoryRoomType.getRoomType().getRoomSize())
                            .room(getRoomNumber(resident))
                            .build();
                })
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
