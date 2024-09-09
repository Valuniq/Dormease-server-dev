package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitory.dto.response.*;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.term.domain.Term;
import dormease.dormeasedev.domain.term.domain.repository.TermRepository;
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
    private final TermRepository termRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final ResidentRepository residentRepository;

    // 건물, 층별 호실 목록 조회
    public ResponseEntity<?> getRoomsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        // 이름이 같은 기숙사 검색
        List<Dormitory> dormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        List<Room> roomList = new ArrayList<>();
        for (Dormitory findDormitory : dormitories) {
            // floor가 999인 경우 모든 방을 가져오고, 그렇지 않은 경우 특정 층의 방을 가져옴
            if (floor == 999) {
                roomList.addAll(roomRepository.findByDormitoryAndIsActivated(findDormitory, true));

            } else {
                roomList.addAll(roomRepository.findByDormitoryAndFloorAndIsActivated(findDormitory, floor, true));
            }

        }
        // 가져온 방들을 처리하여 결과 리스트에 추가
        List<RoomByDormitoryAndFloorRes> rooms = roomList.stream()
                .map(room -> RoomByDormitoryAndFloorRes.builder()
                        .id(room.getId())
                        .roomNumber(room.getRoomNumber())
                        .roomSize(room.getRoomSize())
                        .gender(room.getGender().toString())
                        .currentPeople(room.getCurrentPeople())
                        .build())
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

        // TODO: 테이블 변경에 따른 수정 가능성 있음
        Integer dormitorySize = Optional.ofNullable(dormitory.getDormitorySize()).orElse(0);
        Integer roomCount = Optional.ofNullable(dormitory.getRoomCount()).orElse(0);

        List<Room> rooms = roomRepository.findByDormitoryAndIsActivated(dormitory, true).stream().toList();
        for (Room room : rooms) {
            currentPeopleCount += Optional.ofNullable(room.getCurrentPeople()).orElse(0);

            if (Optional.ofNullable(room.getRoomSize()).orElse(0).equals(room.getCurrentPeople())) {
                fullRoomCount += 1;
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

        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getDormitoryRoomType().getRoomType().getRoomSize());
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
    public ResponseEntity<?> getNotAssignedResidents(CustomUserDetails customUserDetails, Long dormitoryId) {
        User admin = validUserById(customUserDetails.getId());
        Dormitory dormitory = validDormitoryById(dormitoryId);
        List<Resident> notAssignedResidents = new ArrayList<>();
        // dormitory 이름, 성별 같은 기숙사 가져오기
        List<Dormitory> sameNameAndSameGenderDormitories = dormitoryRepository.findBySchoolAndNameAndGender(admin.getSchool(), dormitory.getName(), dormitory.getDormitoryRoomType().getRoomType().getGender());
            // pass && now
            // -> 미배정 사생 조회이므로 resident findByDormitory / 해당 기숙사의 미배정 사생
        List<Resident> residentList = residentRepository.findByDormitoryAndRoom(dormitory, null);

        List<NotOrAssignedResidentRes> notAssignedResidentsResList = new ArrayList<>();
        for (Resident resident : residentList) {
            String studentNumber = null;
            String phoneNumber= null;
            Boolean isAssigned = false;
            if (resident.getUser() != null) {
                studentNumber = resident.getUser().getStudentNumber();
                phoneNumber = resident.getUser().getPhoneNumber();
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
        User admin = validUserById(customUserDetails.getId());
        Room room = validRoomById(roomId);
        List<Resident> assignedResidents = residentRepository.findByRoom(room);
        List<NotOrAssignedResidentRes> assignedResidentRes = assignedResidents.stream()
                .map(resident -> NotOrAssignedResidentRes.builder()
                        .id(resident.getId())
                        .name(resident.getUser().getName())
                        .studentNumber(resident.getUser().getStudentNumber())
                        .phoneNumber(resident.getUser().getPhoneNumber())
                        .isAssigned(checkResidentAssignedToRoom(resident))
                        .build())
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
    public ResponseEntity<?> assignedResidentsToRoom(CustomUserDetails customUserDetails,List<AssignedResidentToRoomReq> assignedResidentToRoomReqList) {
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
                for (int i=1; i<=room.getRoomSize(); i++) {
                   if(!residentRepository.existsByRoomAndBedNumber(room, i)) {
                       bedNumberCount = i;
                       break;
                   }
                }
                DefaultAssert.isTrue(bedNumberCount <= room.getRoomSize(), "배정 가능한 침대가 없습니다.");
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
        room.updateCurrentPeople(currentPeopleCount);
    }
}
