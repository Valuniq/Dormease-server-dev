package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AddRoomNumberReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.RoomSettingReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.DormitorySettingDetailRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.FloorAndRoomNumberRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.RoomSettingRes;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.exception.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitorySettingDetailService {

    private final RoomRepository roomRepository;
    private final DormitoryRepository dormitoryRepository;

    // 호실 개수 추가
    @Transactional
    public ResponseEntity<?> addFloorAndRoomNumber(CustomUserDetails customUserDetails, Long dormitoryId, AddRoomNumberReq addRoomNumberReq) {

        Dormitory dormitory = validDormitoryById(dormitoryId);

        Integer floor = addRoomNumberReq.getFloor();
        Integer start = addRoomNumberReq.getStartRoomNumber();
        Integer end = addRoomNumberReq.getEndRoomNumber();

        // 이미 해당 층이 존재하면 예외처리
        DefaultAssert.isTrue(roomRepository.findByDormitoryAndFloor(dormitory, floor).isEmpty(), "중복된 층이 존재합니다.");

        // 유효성 검사
        verifyRoomNumber(start, end);

        List<Room> rooms = generateRoomNumbers(dormitory, floor, start, end);
        DefaultAssert.isTrue(!rooms.isEmpty(), "호실 생성 중 오류가 발생했습니다.");

        List<RoomSettingRes> roomSettingResList = rooms.stream()
                .map(room -> RoomSettingRes.builder()
                        .id(room.getId())
                        .floor(room.getFloor())
                        .gender(room.getRoomType().getGender().toString())
                        .roomNumber(room.getRoomNumber())
                        .roomSize(room.getRoomType().getRoomSize())
                        .hasKey(room.getHasKey())
                        .isActivated(room.getIsActivated())
                        .build())
                .sorted(Comparator.comparing(RoomSettingRes::getRoomNumber))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomSettingResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    private List<Room> generateRoomNumbers(Dormitory dormitory, int floor, int startRoomNumber, int endRoomNumber) {
        List<Room> rooms = new ArrayList<>();
        for (int roomNumber = startRoomNumber; roomNumber <= endRoomNumber; roomNumber++) {
            int roomNumberInt = Integer.parseInt(floor + String.format("%02d", roomNumber));
            Room room = Room.builder()
                    .dormitory(dormitory)
                    .floor(floor)
                    .currentPeople(0)
                    .roomNumber(roomNumberInt)
                    .build();
            rooms.add(room);
        }
        // 생성한 호실 한번에 저장
        return roomRepository.saveAll(rooms);

    }

    // 건물 상세 조회
    public ResponseEntity<?> getDormitoryDetails(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());

        List<Room> rooms = new ArrayList<>();
        for (Dormitory findDormitory : sameNameDormitories) {
            List<Room> dormitoryRooms = roomRepository.findByDormitory(findDormitory);
            rooms.addAll(dormitoryRooms);
        }
        // 각 층별 호실 정보 저장
        Map<Integer, List<Room>> floorToRoomsMap = rooms.stream()
                .collect(Collectors.groupingBy(room -> extractFloorFromRoomNumber(room.getRoomNumber())));

        // 각 층별 최소/최대 호실 번호를 계산하여 FloorAndRoomNumberRes 객체 생성
        List<FloorAndRoomNumberRes> floorAndRoomNumberResList = floorToRoomsMap.entrySet().stream()
                .map(entry -> {
                    List<Room> roomList = entry.getValue();
                    int minRoomNumber = extractLastTwoDigits(Collections.min(roomList, Comparator.comparing(Room::getRoomNumber)).getRoomNumber());
                    int maxRoomNumber = extractLastTwoDigits(Collections.max(roomList, Comparator.comparing(Room::getRoomNumber)).getRoomNumber());

                    return FloorAndRoomNumberRes.builder()
                            .floor(entry.getKey())
                            .startRoomNumber(minRoomNumber)
                            .endRoomNumber(maxRoomNumber)
                            .build();
                })
                .sorted(Comparator.comparing(FloorAndRoomNumberRes::getFloor))
                .collect(Collectors.toList());

        DormitorySettingDetailRes dormitorySettingDetailRes = DormitorySettingDetailRes.builder()
                .id(dormitoryId)
                .name(dormitory.getName())
                .imageUrl(dormitory.getImageUrl())
                .floorAndRoomNumberRes(floorAndRoomNumberResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitorySettingDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 호실 번호에서 층 수 추출
    private int extractFloorFromRoomNumber(int roomNumber) {
        return roomNumber / 100; // 예시: 101 -> 1층, 201 -> 2층, 305 -> 3층
    }

    // 호실 번호에서 뒤의 두 자리 숫자만 추출하는 메서드
    private Integer extractLastTwoDigits(int roomNumber) {
        return roomNumber % 100;
    }

    // 건물, 층으로 호실 조회
    public ResponseEntity<?> getRoomsByDormitoryAndFloor(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {

        Dormitory dormitory = validDormitoryById(dormitoryId);

        // 이름 같은 기숙사 가져오기
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        // 해당 기숙사의 층별 호실 가져오기
        List<Room> roomList = roomRepository.findByDormitoryInAndFloor(sameNameDormitories, floor);

        List<RoomSettingRes> roomSettingResList = roomList.stream()
                .map(room -> RoomSettingRes.builder()
                        .id(room.getId())
                        .floor(room.getFloor())
                        .gender(room.getRoomType().getGender().toString())
                        .roomNumber(room.getRoomNumber())
                        .roomSize(room.getRoomType().getRoomSize())
                        .hasKey(room.getHasKey())
                        .isActivated(room.getIsActivated())
                        .build())
                .sorted(Comparator.comparing(RoomSettingRes::getRoomNumber))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomSettingResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 호실 삭제
    // 해당 층 가져와서 deleteAll
    @Transactional
    public ResponseEntity<?> deleteRoomsByFloor(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Room> deletedRooms = new ArrayList<>();

        // 이름 같은 기숙사 가져오기
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndName(dormitory.getSchool(), dormitory.getName());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        for (Dormitory sameNameDormitory : sameNameDormitories) {
            List<Room> dormitoryRooms = roomRepository.findByDormitoryAndFloor(sameNameDormitory, floor);
            roomRepository.deleteAll(dormitoryRooms);    // 해당 층의 방 모두 삭제

            deletedRooms.addAll(dormitoryRooms);
        }

        // 수용인원, 호실 개수 업데이트
        updateDormitorySize(deletedRooms);
        updateRoomCount(deletedRooms);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("호실이 삭제되었습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    // 호실 정보 수정
    // 필터
    @Transactional
    public ResponseEntity<?> updateRoomSetting(CustomUserDetails customUserDetails, List<RoomSettingReq> roomSettingReqs) {
        List<Room> updatedRooms = new ArrayList<>();

        for (RoomSettingReq roomSettingReq : roomSettingReqs) {
            Long roomId = roomSettingReq.getId();
            Optional<Room> findRoom = roomRepository.findById(roomId);
            DefaultAssert.isTrue(findRoom.isPresent(), "호실 정보가 올바르지 않습니다.");
            Room room = findRoom.get();

            // 입력 유효성 검사
            validateRoomSetting(room, roomSettingReq);

            // 기존 dormitory의 Gender, roomSize
            // String existingGender = room.getDormitory().getGender().toString();
            // Integer existingRoomSize = room.getDormitory().getRoomSize();

            // 기존 dormitory와 다른 Gender 또는 roomSize인 경우
            // if (!Objects.equals(existingGender, roomSettingReq.getGender()) || !Objects.equals(existingRoomSize, roomSettingReq.getRoomSize())) {
            //    createOrUpdateDormitory(room, roomSettingReq);
            //}

            // 적절한 dormitory에 room 할당
            assignRoomToDormitory(room, roomSettingReq);

            // 호실 정보 업데이트 후 리스트에 추가
            updatedRooms.add(room);

        }

        // 수용인원, 호실 개수 업데이트
        updateDormitorySize(updatedRooms);
        updateRoomCount(updatedRooms);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("호실 정보가 등록되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void assignRoomToDormitory(Room room, RoomSettingReq roomSettingReq) {
        Dormitory dormitory = room.getDormitory();
        // 이름,  GENDER, roomSize 같은 기숙사 조회
        //Dormitory newDormitory = dormitoryRepository.findBySchoolAndNameAndGenderAndRoomSize(dormitory.getSchool(), dormitory.getName(), Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getRoomSize());
        //DefaultAssert.isTrue(newDormitory != null, "해당하는 건물이 없습니다.");
        // room update
        //room.updateRoomSetting(newDormitory, roomSettingReq.getRoomSize(), Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getHasKey());
        //if (roomSettingReq.getIsActivated() != null) { room.updateIsActivated(roomSettingReq.getIsActivated());}

    }

    private void updateDormitorySize(List<Room> rooms) {
        Set<Dormitory> dormitoriesToUpdate = new HashSet<>();

        for (Room room : rooms) {
            Dormitory dormitory = room.getDormitory();
            dormitoriesToUpdate.add(dormitory);
        }

        for (Dormitory dormitory : dormitoriesToUpdate) {
            //Integer roomSize = dormitory.getRoomSize();
            //Integer dormitorySize = roomRepository.findByDormitoryAndIsActivated(dormitory, true).size();
            //dormitory.updateDormitorySize(dormitorySize * roomSize);
        }
    }

    private void updateRoomCount(List<Room> rooms) {
        Set<Dormitory> dormitoriesToUpdate = new HashSet<>();

        for (Room room : rooms) {
            Dormitory dormitory = room.getDormitory();
            dormitoriesToUpdate.add(dormitory);
        }

        for (Dormitory dormitory : dormitoriesToUpdate) {
            Integer dormitorySize = roomRepository.findByDormitoryAndIsActivated(dormitory, true).size();
            dormitory.updateRoomCount(dormitorySize);
        }
    }

    private static void verifyRoomNumber(Integer start, Integer end) {
        // 입력 값 유효성 검사
        DefaultAssert.isTrue(start < 99 || end < 99 || start > 0 || end > 0, "호실 번호는 1부터 99까지의 값이어야 합니다.");
        DefaultAssert.isTrue(start < end, "시작 호실 번호는 끝 호실 번호보다 작거나 같아야 합니다.");
    }

    private void validateRoomSetting(Room room, RoomSettingReq roomSettingReq){
        DefaultAssert.isTrue(room.getId().equals(roomSettingReq.getId()), "호실이 일치하지 않습니다.");

        Gender gender = Gender.valueOf(roomSettingReq.getGender());
        DefaultAssert.isTrue(gender == Gender.MALE || gender == Gender.FEMALE, "호실의 성별이 지정되어있지 않습니다.");
    }

    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }
}
