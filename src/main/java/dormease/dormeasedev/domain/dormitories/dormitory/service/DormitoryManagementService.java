package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.*;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ResidentRepository residentRepository;

    // 건물, 층별 호실 목록 조회
    // Description: 호실 필터 미설정 시 gender는 EMPTY, roomSize는 null
    public List<RoomByDormitoryAndFloorRes> getRoomsByDormitory(UserDetailsImpl userDetailsImpl, Long dormitoryId, Integer floor) {
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
                            .gender(gender)
                            .currentPeople(room.getCurrentPeople())
                            .build();
                })
                .sorted(Comparator.comparingInt(RoomByDormitoryAndFloorRes::getRoomNumber))
                .toList();

        return rooms;
    }

    // 건물 정보 조회
    // TODO: 오류 안나게 null 처리(인실, 방 개수, 총 인원 등)
    public DormitoryManagementDetailRes getDormitoryInfo(UserDetailsImpl userDetailsImpl, Long dormitoryId) {
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

        return DormitoryManagementDetailRes.builder()
                .name(dormitory.getName())
                .imageUrl(Optional.ofNullable(dormitory.getImageUrl()).orElse(""))
                .fullRoomCount(fullRoomCount)
                .roomCount(roomCount)
                .currentPeopleCount(currentPeopleCount)
                .dormitorySize(dormitorySize)
                .memo(Optional.ofNullable(dormitory.getMemo()).orElse(""))
                .build();
    }

    // 학교별 건물 목록 조회(건물명)
    public List<DormitoryManagementListRes> getDormitoriesByRoomSize(UserDetailsImpl userDetailsImpl) {
        User user = validUserById(userDetailsImpl.getUserId());

        List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());
        List<DormitoryManagementListRes> dormitoryManagementListRes = dormitories.stream()
                .map(dormitory -> DormitoryManagementListRes.builder()
                        .id(dormitory.getId())
                        .name(dormitory.getName())
                        .build())
                .sorted(Comparator.comparing(DormitoryManagementListRes::getName))
                .toList();

        return dormitoryManagementListRes;
    }

    // 건물별 층 수 목록 조회
    public List<FloorByDormitoryRes> getFloorsByDormitory(UserDetailsImpl userDetailsImpl, Long dormitoryId) {
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

        return floorByDormitoryResList;
    }


    // 배정된 호실이 없는 사생 목록 조회
    public List<NotOrAssignedResidentRes> getNotAssignedResidents(Long roomId) {
        Room room = validRoomById(roomId);
        Dormitory dormitory = validDormitoryById(room.getDormitory().getId());

        // Gender gender = room.getRoomType().getGender();
        // 호실의 성별, 인실, 기숙사가 일치해야함
        // dormitoryTerm 중에 dormitoryRoomType이 일치하는 것만 가져오기
        RoomType roomType = room.getRoomType();
        DormitoryRoomType dormitoryRoomType = dormitoryRoomTypeRepository.findByDormitoryAndRoomType(dormitory, roomType);
        // 호실 배정시 term은 고려하지 않아도 되는지?
        List<DormitoryTerm> dormitoryTerms = dormitoryTermRepository.findByDormitoryRoomType(dormitoryRoomType);
        // 반복문 add
        List<Resident> residentList = new ArrayList<>();
        for (DormitoryTerm dormitoryTerm : dormitoryTerms) {
            List<Resident> residents = residentRepository.findByDormitoryTermAndRoom(dormitoryTerm, null);
            residentList.addAll(residents);
        }

        List<NotOrAssignedResidentRes> notAssignedResidentsResList = residentList.stream()
                .map(resident -> {
                    Student student = resident.getStudent();
                    return NotOrAssignedResidentRes.builder()
                            .id(resident.getId())
                            .studentNumber(student != null ? student.getStudentNumber() : null)
                            .name(resident.getName())
                            .phoneNumber(student != null ? student.getPhoneNumber() : null)
                            .isAssigned(resident.getRoom() != null) // 호실 거주 여부 / 무조건 false여야 함
                            .build();}
                )
                .collect(Collectors.toList());

        return notAssignedResidentsResList;
    }

    // 해당 호실에 거주하는 사생 조회
    public List<NotOrAssignedResidentRes> getAssignedResidents(UserDetailsImpl userDetailsImpl, Long roomId) {
        Room room = validRoomById(roomId);
        List<Resident> assignedResidents = residentRepository.findByRoom(room);
        List<NotOrAssignedResidentRes> assignedResidentRes = assignedResidents.stream()
                .map(resident -> {
                    Student student = resident.getStudent();
                    return NotOrAssignedResidentRes.builder()
                            .id(resident.getId())
                            .name(resident.getName())
                            .studentNumber(student != null ? student.getStudentNumber() : null)
                            .phoneNumber(student != null ? student.getPhoneNumber() : null)
                            .isAssigned(resident.getRoom() != null)
                            .build();
                })
                .collect(Collectors.toList());

        return assignedResidentRes;
    }

    // 수기 방배정
    @Transactional
    public void assignedResidentsToRoom(Long roomId, AssignedResidentToRoomReq assignedResidentToRoomReq) {
        Room room = validRoomById(roomId);

        // room에 배정된 사생 가져와서 사생의 room null처리
        List<Resident> residents = residentRepository.findByRoom(room);
        if (!residents.isEmpty()) {
            for (Resident resident : residents) {
                resident.updateRoom(null);
                resident.updateBedNumber(null);
            }
        }

        for (Long residentId : assignedResidentToRoomReq.getResidentIds()) {
            Optional<Resident> residentOpt = residentRepository.findById(residentId);
            DefaultAssert.isTrue(residentOpt.isPresent(), "사생 정보가 올바르지 않습니다.");
            Resident resident = residentOpt.get();

            resident.updateRoom(room);
            resident.updateBedNumber(assignedBedNumber(room));
        }
        // currentPeople update
        updateCurrentPeople(room);
    }


    // 침대번호 지정 메소드
    private int assignedBedNumber(Room room) {
        int bedNumberCount = Integer.MAX_VALUE;
        for (int i=1; i<=room.getRoomType().getRoomSize(); i++) {
            // 건물 설정 - 필터가 완료되어야 사생을 가질 수 있으므로, room.getRoomTYpe()이 null일 경우 존재하지 않음
            if(!residentRepository.existsByRoomAndBedNumber(room, i)) {
                bedNumberCount = i;
                break;
            }
        }
        DefaultAssert.isTrue(bedNumberCount <= room.getRoomType().getRoomSize(), "배정 가능한 침대가 없습니다.");
        return bedNumberCount;
    }

    // 건물별 메모 저장
    @Transactional
    public void registerDormitoryMemo(Long dormitoryId, DormitoryMemoReq dormitoryMemoReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        dormitory.updateMemo(dormitoryMemoReq.getMemo());
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
