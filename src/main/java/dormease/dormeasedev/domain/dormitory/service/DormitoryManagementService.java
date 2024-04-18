package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitory.dto.response.*;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
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
import lombok.RequiredArgsConstructor;
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
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final ResidentRepository residentRepository;

    // 건물, 층별 호실 목록 조회
    public ResponseEntity<?> getRoomsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Dormitory> dormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getRoomSize());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        List<RoomByDormitoryAndFloorRes> roomByDormitoryAndFloorRes = new ArrayList<>();
        for (Dormitory findDormitory : dormitories) {
            List<Room> rooms = roomRepository.findByDormitoryAndFloorAndIsActivated(findDormitory, floor, true);

            roomByDormitoryAndFloorRes.addAll(
                    rooms.stream()
                            .map(room -> RoomByDormitoryAndFloorRes.builder()
                                    .id(room.getId())
                                    .roomNumber(room.getRoomNumber())
                                    .roomSize(room.getRoomSize())
                                    .gender(room.getGender().toString())
                                    .currentPeople(room.getCurrentPeople())
                                    .build())
                            .toList());
        }

        // 호실 번호로 오름차순 정렬
        roomByDormitoryAndFloorRes.sort(Comparator.comparingInt(RoomByDormitoryAndFloorRes::getRoomNumber));

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

    // 학교별 건물 목록 조회(건물명(n인실))
    public ResponseEntity<?> getDormitoriesByRoomSize(CustomUserDetails customUserDetails) {
        User user = validateUserById(customUserDetails.getId());

        List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Set<String> existingDormitoryNames = new HashSet<>();
        List<DormitoryManagementListRes> dormitoryManagementListRes = new ArrayList<>();

        for (Dormitory dormitory : dormitories) {
            String key = dormitory.getName() + "(" + dormitory.getRoomSize() + "인실)";
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

            uniqueFloorNumbers.addAll(floorNumbers); // 중복 제거를 위해 Set에 층 수 추가
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
    public ResponseEntity<?> getNotAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {
        Room room = validateRoomById(roomId);

        List<Resident> notAssignedResidents = new ArrayList<>();

        List<DormitorySettingTerm> dormitorySettingTerms = dormitorySettingTermRepository.findByDormitory(room.getDormitory());
        DefaultAssert.isTrue(!dormitorySettingTerms.isEmpty()); // 오류 메세지 고민 필요

        for (DormitorySettingTerm dormitorySettingTerm : dormitorySettingTerms) {
            // 미배정된 사생만
            List<Resident> residents = residentRepository.findByDormitorySettingTermAndRoom(dormitorySettingTerm, null);
            for (Resident resident : residents) {
                if (resident.getUser().getGender() == room.getGender()) {
                    notAssignedResidents.add(resident);
                }
            }
        }

        List<NotOrAssignedResidentsRes> notOrAssignedResidentsResList = notAssignedResidents.stream()
                .map(resident -> NotOrAssignedResidentsRes.builder()
                        .id(resident.getId())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .name(resident.getUser().getName())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notOrAssignedResidentsResList).build();

        return  ResponseEntity.ok(apiResponse);

    }

    // 해당 호실에 거주하는 사생 조회
    public ResponseEntity<?> getAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {
        Room room = validateRoomById(roomId);

        List<Resident> assignedResidents = residentRepository.findByRoom(room);
        List<NotOrAssignedResidentsRes> assignedResidentsResList = assignedResidents.stream()
                .map(resident -> NotOrAssignedResidentsRes.builder()
                        .id(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(assignedResidentsResList).build();

        return  ResponseEntity.ok(apiResponse);
    }

    // 수기 방배정
    @Transactional
    public ResponseEntity<?> assignedResidentsToRoom(CustomUserDetails customUserDetails, Long roomId, List<AssignedResidentToRoomReq> assignedResidentToRoomReqList) {
        Room room = validateRoomById(roomId);
        Integer bedNumberCount = 0;
        if (room.getCurrentPeople() != null) { bedNumberCount = room.getCurrentPeople(); }

        for (AssignedResidentToRoomReq req : assignedResidentToRoomReqList) {
            bedNumberCount += 1;
            DefaultAssert.isTrue(bedNumberCount <= room.getRoomSize(), "배정 가능한 인원이 초과되었습니다.");

            Optional<Resident> residentOpt = residentRepository.findById(req.getId());
            DefaultAssert.isTrue(residentOpt.isPresent(), "사생 정보가 올바르지 않습니다.");
            Resident resident = residentOpt.get();

            resident.updateRoom(room);
            resident.updateBedNumber(bedNumberCount);
        }
        // currentPeople update
        updateCurrentPeople(room);

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
        // 같은 이름의 같은 인실인 기숙사만 가져오기
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

    public Room validateRoomById(Long roomId) {
        Optional<Room> findRoom = roomRepository.findById(roomId);
        DefaultAssert.isTrue(findRoom.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findRoom.get();
    }

    public User validateUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }

    private void updateCurrentPeople(Room room) {
        Integer currentPeopleCount = residentRepository.findByRoom(room).size();
        DefaultAssert.isTrue(currentPeopleCount < room.getRoomSize(), "배정 가능한 인원을 초과했습니다.");
        room.updateCurrentUser(currentPeopleCount);
    }


}
