package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitory.dto.request.ResidentIdReq;
import dormease.dormeasedev.domain.dormitory.dto.response.*;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitoryManagementService {

    private final DormitoryRepository dormitoryRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final ResidentRepository residentRepository;

    // 건물, 층별 호실 목록 조회
    // TODO: 999 받으면 전체호실
    public ResponseEntity<?> getRoomsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        // 이름이 같은 기숙사 검색
        List<Dormitory> dormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        // 호실 정보 조회
        List<RoomByDormitoryAndFloorRes> roomByDormitoryAndFloorRes = new ArrayList<>();
        for (Dormitory findDormitory : dormitories) {
            List<Room> roomList = roomRepository.findByDormitoryAndFloorAndIsActivated(findDormitory, floor, true);

            List<RoomByDormitoryAndFloorRes> rooms = roomList.stream()
                    .map(room -> RoomByDormitoryAndFloorRes.builder()
                            .id(room.getId())
                            .roomNumber(room.getRoomNumber())
                            .roomSize(room.getRoomSize())
                            .gender(room.getGender().toString())
                            .currentPeople(room.getCurrentPeople())
                            .build())
                    .sorted(Comparator.comparing(RoomByDormitoryAndFloorRes::getRoomNumber)) // roomNumber 오름차순 정렬
                    .toList();
            roomByDormitoryAndFloorRes.addAll(rooms);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomByDormitoryAndFloorRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물 정보 조회
    public ResponseEntity<?> getDormitoryInfo(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 건물명, 메모, 이미지는 dormitory에서 가져오기

        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Integer fullRoomCount = 0;
        Integer roomCount = 0;
        Integer currentPeopleCount = 0;
        Integer dormitorySize = 0;

        for (Dormitory findDormitory : sameNameDormitories) {
            dormitorySize += findDormitory.getDormitorySize();
            roomCount += findDormitory.getRoomCount();

            List<Room> rooms = roomRepository.findByDormitoryAndIsActivated(findDormitory, true).stream().toList();
            for (Room room : rooms) {
                currentPeopleCount += room.getCurrentPeople();
                // 인실과 현재 수용된 인원이 동일할 시 꽉 찬 호실 수 추가
                if (room.getRoomSize() == room.getCurrentPeople()) {
                    fullRoomCount += 1;
                }
            }

        }

        DormitoryManagementDetailRes dormitoryManagementDetailRes = DormitoryManagementDetailRes.builder()
                .name(dormitory.getName())
                .imageUrl(dormitory.getImageUrl())
                .fullRoomCount(fullRoomCount)
                .roomCount(roomCount)
                .currentPeopleCount(currentPeopleCount)
                .dormitorySize(dormitorySize)
                .memo(dormitory.getMemo())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementDetailRes).build();

        return ResponseEntity.ok(apiResponse);

    }

    // 학교별 건물 목록 조회(건물명)
    public ResponseEntity<?> getDormitoriesByRoomSize(CustomUserDetails customUserDetails) {
        User user = validUserById(customUserDetails.getId());

        List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Set<String> existingDormitoryNames = new HashSet<>();
        List<DormitoryManagementListRes> dormitoryManagementListRes = new ArrayList<>();

        for (Dormitory dormitory : dormitories) {
            String key = dormitory.getName();
            if (existingDormitoryNames.add(key)) {
                dormitoryManagementListRes.add(
                        DormitoryManagementListRes.builder()
                        .id(dormitory.getId())
                        .name(key).build());
            }
        }

        // 건물명 오름차순 정렬
        Comparator<DormitoryManagementListRes> comparator = Comparator.comparing(DormitoryManagementListRes::getName);
        dormitoryManagementListRes.sort(comparator);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물별 층 수 목록 조회
    public ResponseEntity<?> getFloorsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getRoomSize());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Set<Integer> uniqueFloorNumbers = new HashSet<>();

        for (Dormitory findDormitory : sameNameDormitories) {
            List<Integer> floorNumbers = roomRepository.findByDormitoryAndIsActivated(findDormitory, true).stream()
                    .map(Room::getFloor)
                    .distinct()
                    .sorted()
                    .toList();

            uniqueFloorNumbers.addAll(floorNumbers);
        }

        List<FloorByDormitoryRes> floorByDormitoryResList = uniqueFloorNumbers.stream()
                .map(floor -> FloorByDormitoryRes.builder()
                        .floor(floor)
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(floorByDormitoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // 배정된 호실이 없는 사생 목록 조회
    // TODO : 해당 방에 배정된 사생도 조회
    public ResponseEntity<?> getNotAndAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {
        Room room = validRoomById(roomId);
        List<Resident> notAssignedResidents = new ArrayList<>();
        // 기숙사 - 거주 기간 - 입사 신청 - 회원 - 사생
        List<DormitoryTerm> dormitoryTerms = dormitoryTermRepository.findByDormitory(room.getDormitory());

        Set<User> users = new HashSet<>();
        for (DormitoryTerm term : dormitoryTerms) {
            List<DormitoryApplication> findDormitoryApplications = dormitoryApplicationRepository.findByDormitoryTerm(term);
            for (DormitoryApplication application : findDormitoryApplications) {
                users.add(application.getUser());
            }
        }
        for (User user : users) {
            Resident resident = residentRepository.findByUserAndRoom(user, null);
            if (resident != null) {
                notAssignedResidents.add(resident);
            }
        }

       List<NotAssignedResidentRes> notAssignedResidentsResList = notAssignedResidents.stream()
                .map(resident -> NotAssignedResidentRes.builder()
                        .id(resident.getId())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .name(resident.getUser().getName())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .isAssigned(checkResidentAssignedToRoom(resident))
                        .build())
                .collect(Collectors.toList());
        // 배정된 사생 조회
        List<AssignedResidentRes> assignedResidentsResList = assignedResidentsByRoom(room);

        NotOrAssignedResidentsRes residentsRes = NotOrAssignedResidentsRes.builder()
                .assignedResidentResList(assignedResidentsResList)
                .notAssignedResidentResList(notAssignedResidentsResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentsRes)
                .build();

        return  ResponseEntity.ok(apiResponse);

    }

    // 해당 호실에 거주하는 사생 조회
    public ResponseEntity<?> getAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {
        Room room = validRoomById(roomId);
        List<AssignedResidentRes> assignedResidentResList = assignedResidentsByRoom(room);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(assignedResidentResList).build();

        return  ResponseEntity.ok(apiResponse);
    }

    // 배정된 사생 조회 메소드
    private List<AssignedResidentRes> assignedResidentsByRoom(Room room) {
        List<Resident> assignedResidents = residentRepository.findByRoom(room);
        return assignedResidents.stream()
                .map(resident -> AssignedResidentRes.builder()
                        .id(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .isAssigned(checkResidentAssignedToRoom(resident))
                        .build())
                .collect(Collectors.toList());
    }

    // 사생의 방 배정 여부 체크 메소드
    private boolean checkResidentAssignedToRoom(Resident resident) {
        Room findRoom = resident.getRoom();
        return findRoom != null;
    }


    // 수기 방배정
    // TODO: 침대번호 배정 로직 다시 생각할 것 / 배정 취소 및 배정 가능
    // ex. 1, 2번 중에 1번이 빠졌으면 1에 배정해야 하는데 현재 로직으로는 이거 불가능
    @Transactional
    public ResponseEntity<?> assignedResidentsToRoom(CustomUserDetails customUserDetails,List<AssignedResidentToRoomReq> assignedResidentToRoomReqList) {
        // 리스트 사이즈만큼 반복
        for (AssignedResidentToRoomReq assignedResidentToRoomReq : assignedResidentToRoomReqList) {
            Room room = validRoomById(assignedResidentToRoomReq.getRoomId());
            Integer bedNumberCount = room.getCurrentPeople();

            for (ResidentIdReq residentIdReq : assignedResidentToRoomReq.getResidentIdReqList()) {
                bedNumberCount += 1;
                DefaultAssert.isTrue(bedNumberCount <= room.getRoomSize(), "배정 가능한 인원을 초과했습니다.");

                Optional<Resident> residentOpt = residentRepository.findById(residentIdReq.getId());
                DefaultAssert.isTrue(residentOpt.isPresent(), "사생 정보가 올바르지 않습니다.");
                Resident resident = residentOpt.get();

                resident.updateRoom(room);
                resident.updateBedNumber(bedNumberCount);
            }
            // currentPeople update
            updateCurrentPeople(room);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("호실이 배정되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 건물별 메모 저장
    @Transactional
    public ResponseEntity<?> registerDormitoryMemo(CustomUserDetails customUserDetails, Long dormitoryId, DormitoryMemoReq dormitoryMemoReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        for (Dormitory updateDormitory : sameNameDormitories) {
            updateDormitory.updateMemo(dormitoryMemoReq.getMemo());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("메모가 저장되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }

    public Room validRoomById(Long roomId) {
        Optional<Room> findRoom = roomRepository.findById(roomId);
        DefaultAssert.isTrue(findRoom.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findRoom.get();
    }

    public User validUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }

    private void updateCurrentPeople(Room room) {
        Integer currentPeopleCount = residentRepository.findByRoom(room).size();
        DefaultAssert.isTrue(currentPeopleCount <= room.getRoomSize(), "배정 가능한 인원을 초과했습니다.");
        room.updateCurrentUser(currentPeopleCount);
    }


}
