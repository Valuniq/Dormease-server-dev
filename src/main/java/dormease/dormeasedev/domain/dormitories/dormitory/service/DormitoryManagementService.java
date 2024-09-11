package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.*;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitoryManagementService {

    private final DormitoryRepository dormitoryRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ResidentRepository residentRepository;

    // 건물, 층별 호실 목록 조회
    // Description: 호실 필터 미설정 시 gender는 EMPTY, roomSize는 null
    public ResponseEntity<?> getRoomsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Room> roomList = new ArrayList<>();
        // floor가 999인 경우 모든 방을 가져오고, 그렇지 않은 경우 특정 층의 방을 가져옴
        if (floor == 999) {
            roomList.addAll(roomRepository.findByDormitoryAndIsActivated(dormitory, true));

        } else {
            roomList.addAll(roomRepository.findByDormitoryAndFloorAndIsActivated(dormitory, floor, true));
        }

        // 가져온 방들을 처리하여 결과 리스트에 추가
        List<RoomByDormitoryAndFloorRes> rooms = roomList.stream()
                .map(room -> {
                    Integer roomSize = null;
                    Gender gender = Gender.EMPTY;
                    if (room.getRoomType() != null) {
                        roomSize = room.getRoomType().getRoomSize();
                        gender = room.getRoomType().getGender();
                    }
                    return RoomByDormitoryAndFloorRes.builder()
                            .id(room.getId())
                            .roomNumber(room.getRoomNumber())
                            .roomSize(roomSize)
                            .gender(gender.toString())
                            .currentPeople(room.getCurrentPeople())
                            .build();
                })
                .sorted(Comparator.comparingInt(RoomByDormitoryAndFloorRes::getRoomNumber))
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(rooms)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물 정보 조회
    // TODO: 오류 안나게 null 처리(인실, 방 개수, 총 인원 등)
    public ResponseEntity<?> getDormitoryInfo(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 건물명, 메모, 이미지는 dormitory에서 가져오기
        // DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Integer fullRoomCount = 0;
        Integer currentPeopleCount = 0;
        Integer dormitorySize = Optional.ofNullable(dormitory.getDormitorySize()).orElse(0);
        Integer roomCount = Optional.ofNullable(dormitory.getRoomCount()).orElse(0);

        List<Room> rooms = roomRepository.findByDormitoryAndIsActivated(dormitory, true).stream().toList();
        for (Room room : rooms) {
            currentPeopleCount += Optional.ofNullable(room.getCurrentPeople()).orElse(0);

            // 방 타입이 null이면 fullRoomCount를 0으로 설정
            RoomType roomType = room.getRoomType();
            if (roomType != null) {
                Integer roomSize = Optional.ofNullable(roomType.getRoomSize()).orElse(0);
                if (roomSize.equals(room.getCurrentPeople())) {
                    fullRoomCount += 1;
                }
            } else {
                fullRoomCount = 0;
            }
        }

        DormitoryManagementDetailRes dormitoryManagementDetailRes = DormitoryManagementDetailRes.builder()
                .name(dormitory.getName())
                .imageUrl(Optional.ofNullable(dormitory.getImageUrl()).orElse(""))
                .fullRoomCount(fullRoomCount)
                .roomCount(roomCount)
                .currentPeopleCount(currentPeopleCount)
                .dormitorySize(dormitorySize)
                .memo(Optional.ofNullable(dormitory.getMemo()).orElse(""))
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
        List<DormitoryManagementListRes> dormitoryManagementListRes = dormitories.stream()
                .map(dormitory -> DormitoryManagementListRes.builder()
                        .id(dormitory.getId())
                        .name(dormitory.getName())
                        .build())
                .sorted(Comparator.comparing(DormitoryManagementListRes::getName))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물별 층 수 목록 조회
    public ResponseEntity<?> getFloorsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // Set<Integer> uniqueFloorNumbers = new HashSet<>();

        List<Integer> floorNumbers = roomRepository.findByDormitoryAndIsActivated(dormitory, true).stream()
                .map(Room::getFloor)
                .distinct()
                .sorted()
                .toList();
        // uniqueFloorNumbers.addAll(floorNumbers);

        List<FloorByDormitoryRes> floorByDormitoryResList = floorNumbers.stream()
                .map(floor -> FloorByDormitoryRes.builder()
                        .floor(floor)
                        .build())
                .collect(Collectors.toList());

        // 층 수 데이터가 있다면 999(전체) 값을 0번째 인덱스에 추가
        if (!floorByDormitoryResList.isEmpty()) {
            floorByDormitoryResList.add(0, FloorByDormitoryRes.builder()
                    .floor(999)
                    .build());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(floorByDormitoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // 배정된 호실이 없는 사생 목록 조회
    // TODO: 입사신청을 하지않은 호실 미배정 사생의 조회가 가능한지?
    public ResponseEntity<?> getNotAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {    // roomId 받아야함
        Room room = validRoomById(roomId);
        Dormitory dormitory = validDormitoryById(room.getDormitory().getId());

        Gender gender = room.getRoomType().getGender();
        List<Resident> residentList = residentRepository.findByDormitoryAndRoomAndGender(dormitory, null, gender);

        List<NotOrAssignedResidentRes> notAssignedResidentsResList = new ArrayList<>();
        for (Resident resident : residentList) {
            String studentNumber = null;
            String phoneNumber= null;

            User user = resident.getUser();
            // 회원가입 여부 확인
            if (user != null) {
                studentNumber = user.getStudentNumber();
                phoneNumber = user.getPhoneNumber();
            }
            NotOrAssignedResidentRes notOrAssignedResidentRes = NotOrAssignedResidentRes.builder()
                    .id(resident.getId()) // 사생 id
                    .studentNumber(studentNumber)
                    .name(resident.getName())
                    .phoneNumber(phoneNumber)
                    .isAssigned(checkResidentAssignedToRoom(resident)) // 호실 거주 여부 / 무조건 false여야 함
                    .build();
            notAssignedResidentsResList.add(notOrAssignedResidentRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(notAssignedResidentsResList)
                .build();

        return  ResponseEntity.ok(apiResponse);
    }

    // 해당 호실에 거주하는 사생 조회
    public ResponseEntity<?> getAssignedResidents(CustomUserDetails customUserDetails, Long roomId) {
        Room room = validRoomById(roomId);
        List<Resident> assignedResidents = residentRepository.findByRoom(room);
        List<NotOrAssignedResidentRes> assignedResidentRes = assignedResidents.stream()
                .map(resident -> {
                    User user = resident.getUser();
                    return NotOrAssignedResidentRes.builder()
                            .id(resident.getId())
                            .name(user.getName())
                            .studentNumber(user.getStudentNumber())
                            .phoneNumber(user.getPhoneNumber())
                            .isAssigned(checkResidentAssignedToRoom(resident))
                            .build();
                })
                .collect(Collectors.toList());


        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(assignedResidentRes).build();

        return  ResponseEntity.ok(apiResponse);
    }

    // 사생의 방 배정 여부 체크 메소드
    private boolean checkResidentAssignedToRoom(Resident resident) {
        Room findRoom = resident.getRoom();
        return findRoom != null;
    }


    // 수기 방배정(배정 취소 및 배정 가능)
    @Transactional
    public ResponseEntity<?> assignedResidentsToRoom(CustomUserDetails customUserDetails, List<AssignedResidentToRoomReq> assignedResidentToRoomReqList) {
        // 리스트 사이즈만큼 반복
        for (AssignedResidentToRoomReq assignedResidentToRoomReq : assignedResidentToRoomReqList) {
            Room room = validRoomById(assignedResidentToRoomReq.getRoomId());

            // room에 배정된 사생 가져와서 사생의 room null처리
            List<Resident> residents = residentRepository.findByRoom(room);
            if (!residents.isEmpty()) {
                for (Resident resident : residents) {
                    resident.updateRoom(null);
                    resident.updateBedNumber(null);
                }
            }

            int bedNumberCount = Integer.MAX_VALUE;
            for (Long residentId : assignedResidentToRoomReq.getResidentIds()) {
                // 인실 만큼 bedNumber 반복 room과 bedNumber로 사생 찾아서 없으면 해당 bedNumber에 배정
                for (int i=1; i<=room.getRoomType().getRoomSize(); i++) {
                    // 건물 설정 - 필터가 완료되어야 사생을 가질 수 있으므로, room.getRoomTYpe()이 null일 경우X
                   if(!residentRepository.existsByRoomAndBedNumber(room, i)) {
                       bedNumberCount = i;
                       break;
                   }
                }
                DefaultAssert.isTrue(bedNumberCount <= room.getRoomType().getRoomSize(), "배정 가능한 침대가 없습니다.");
                // bedNumberCount += 1;
                // DefaultAssert.isTrue(bedNumberCount <= room.getRoomSize(), "배정 가능한 인원을 초과했습니다.");

                Optional<Resident> residentOpt = residentRepository.findById(residentId);
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
        dormitory.updateMemo(dormitoryMemoReq.getMemo());

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
        DefaultAssert.isTrue(currentPeopleCount <= room.getRoomType().getRoomSize(), "배정 가능한 인원을 초과했습니다.");
        room.updateCurrentPeople(currentPeopleCount);
    }
}
