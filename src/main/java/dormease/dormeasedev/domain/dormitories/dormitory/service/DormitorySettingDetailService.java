package dormease.dormeasedev.domain.dormitories.dormitory.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.CopyRoomsReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.CreateRoomSettingReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.UpdateRoomSettingReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.DormitorySettingDetailRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.FloorAndRoomNumberRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.RoomSettingRes;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitories.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitories.room_type.domain.repository.RoomTypeRepository;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.user.domain.Gender;
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
public class DormitorySettingDetailService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final DormitoryRepository dormitoryRepository;
    private final ResidentRepository residentRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;

    // 호실 생성
    //@Transactional
    //public ResponseEntity<?> addFloorAndRoomNumber(CustomUserDetails customUserDetails, Long dormitoryId, AddRoomNumberReq addRoomNumberReq) {
    //    Dormitory dormitory = validDormitoryById(dormitoryId);
    //    Integer floor = addRoomNumberReq.getFloor();
    //    Integer start = addRoomNumberReq.getStartRoomNumber();
    //    Integer end = addRoomNumberReq.getEndRoomNumber();
        // 이미 해당 층이 존재하면 예외처리
    //    DefaultAssert.isTrue(roomRepository.findByDormitoryAndFloor(dormitory, floor).isEmpty(), "중복된 층이 존재합니다.");
        // 유효성 검사
    //    verifyRoomNumber(start, end);
    //    List<Room> rooms = generateRoomNumbers(dormitory, floor, start, end);
    //    DefaultAssert.isTrue(!rooms.isEmpty(), "호실 생성 중 오류가 발생했습니다.");
        // RoomCount 업데이트
    //    dormitory.updateRoomCount(rooms.size());
    //    List<RoomSettingRes> roomSettingResList = makeRoomSettingRes(rooms);
    //    ApiResponse apiResponse = ApiResponse.builder()
    //            .check(true)
    //            .information(roomSettingResList)
    //            .build();

    //    return ResponseEntity.ok(apiResponse);
    //}

    // 복제
    @Transactional
    public ResponseEntity<?> copyRoomsByFloor(CustomUserDetails customUserDetails, Long dormitoryId, CopyRoomsReq copyRoomsReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        DefaultAssert.isTrue(roomRepository.findByDormitoryAndFloor(dormitory, copyRoomsReq.getNewFloor()).isEmpty(), "중복된 층이 존재합니다.");
        // 복제할 호실 리스트 저장
        List<Room> rooms = roomRepository.findByDormitoryAndFloor(dormitory, copyRoomsReq.getOriginalFloor());
        // 호실 번호만 변경해서 저장
        List<Room> copyRooms = rooms.stream()
                .map(room -> {
                    Integer extractNumber = extractLastTwoDigits(room.getRoomNumber());
                    Integer roomNumber = Integer.valueOf(copyRoomsReq.getNewFloor().toString() + formatTwoDigits(extractNumber));
                    return Room.builder()
                            .dormitory(room.getDormitory())
                            .floor(copyRoomsReq.getNewFloor())
                            .roomNumber(roomNumber)
                            .roomType(room.getRoomType())
                            .isActivated(room.getIsActivated())
                            .hasKey(room.getHasKey())
                            .currentPeople(0)
                            .build();
                })
                .toList();
        roomRepository.saveAll(copyRooms);

        // RoomCount, DormitorySize 업데이트
        updateRoomCount(copyRooms);
        updateDormitorySize(copyRooms);

        List<RoomSettingRes> roomSettingResList = makeRoomSettingRes(copyRooms);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomSettingResList)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    private List<RoomSettingRes> makeRoomSettingRes(List<Room> rooms) {
        return rooms.stream()
                .map(room -> {
                    Integer roomSize = null;
                    Gender gender = Gender.EMPTY;
                    RoomType roomType = room.getRoomType();
                    if (roomType != null) {
                        roomSize = roomType.getRoomSize();
                        gender = roomType.getGender();
                    }
                    return RoomSettingRes.builder()
                            .id(room.getId())
                            .floor(room.getFloor())
                            .gender(gender.toString())
                            .roomNumber(room.getRoomNumber())
                            .roomSize(roomSize)
                            .hasKey(room.getHasKey())
                            .isActivated(room.getIsActivated())
                            .build();
                })
                .sorted(Comparator.comparing(RoomSettingRes::getRoomNumber))
                .collect(Collectors.toList());
    }


    // 건물 상세 조회
    public ResponseEntity<?> getDormitoryDetails(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Integer> floorNumbers = roomRepository.findByDormitory(dormitory).stream()
                .map(Room::getFloor)
                .distinct()
                .sorted()
                .toList();

        List<FloorAndRoomNumberRes> floorAndRoomNumberResList = new ArrayList<>();
        // 층별 최대/최소 호실 번호 추출
        for (Integer floor : floorNumbers) {
            Integer max = extractLastTwoDigits(roomRepository.findMaxRoomNumberByDormitoryAndFloor(dormitory, floor));
            Integer min = extractLastTwoDigits(roomRepository.findMinRoomNumberByDormitoryAndFloor(dormitory, floor));
            floorAndRoomNumberResList.add(
                    FloorAndRoomNumberRes.builder()
                            .floor(floor)
                            .startRoomNumber(min)
                            .endRoomNumber(max)
                            .build()
            );
        }
        // 정렬
        floorAndRoomNumberResList.sort(Comparator.comparing(FloorAndRoomNumberRes::getFloor));

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

    // 호실 번호에서 뒤의 두 자리 숫자만 추출하는 메서드
    private Integer extractLastTwoDigits(int roomNumber) {
        return roomNumber % 100;
    }

    private String formatTwoDigits(int roomNumber) {
        return String.format("%02d", roomNumber);
    }

    // 건물, 층으로 호실 조회
    public ResponseEntity<?> getRoomsByDormitoryAndFloor(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {

        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 해당 기숙사의 층별 호실 가져오기
        List<Room> roomList = roomRepository.findByDormitoryAndFloor(dormitory, floor);

        List<RoomSettingRes> roomSettingResList = makeRoomSettingRes(roomList);

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
        List<Room> deletedRooms = roomRepository.findByDormitoryAndFloor(dormitory, floor);

        boolean hasRelatedResidentByFloor = !hasRelatedResidents(dormitory, floor);
        boolean check = true;
        String msg = "호실이 삭제되었습니다.";
        if (hasRelatedResidentByFloor) {
            check = false;
            msg = "해당 층에 배정된 사생이 있습니다.";
        } else {
            roomRepository.deleteAll(deletedRooms);    // 해당 층의 방 모두 삭제

            // 수용인원, 호실 개수 업데이트
            updateDormitorySize(deletedRooms);
            updateRoomCount(deletedRooms);
            // dormitoryRoomType 업데이트
            deleteDormitoryRoomTypes(dormitory);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(check)
                .information(Message.builder().message(msg).build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean hasRelatedResidents(Dormitory dormitory, Integer floor) {
        List<Room> rooms = roomRepository.findByDormitoryAndFloor(dormitory, floor);
        return residentRepository.existsByRoomIn(rooms);
    }

    // 호실 생성
    @Transactional
    public void saveRoomSetting(Long dormitoryId, Integer floor, List<CreateRoomSettingReq> createRoomSettingReqs) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 이미 해당 층이 존재하면 예외처리
        DefaultAssert.isTrue(roomRepository.findByDormitoryAndFloor(dormitory, floor).isEmpty(), "중복된 층이 존재합니다.");

        List<Room> rooms = generateRooms(dormitory, floor, createRoomSettingReqs);
        DefaultAssert.isTrue(checkValidateRoom(dormitory, floor), "설정되지 않은 속성이 있습니다.");
        // RoomCount 업데이트
        dormitory.updateRoomCount(rooms.size());
    }

    private List<Room> generateRooms(Dormitory dormitory, int floor, List<CreateRoomSettingReq> createRoomSettingReqs) {
        List<Room> rooms = new ArrayList<>();
        for (CreateRoomSettingReq req : createRoomSettingReqs) {
            RoomType roomType = roomTypeRepository.findByRoomSizeAndGender(req.getRoomSize(), req.getGender());
            Room room = Room.builder()
                    .dormitory(dormitory)
                    .floor(floor)
                    .roomNumber(req.getRoomNumber())
                    .roomType(roomType)
                    .hasKey(req.getHasKey())
                    .currentPeople(0)
                    .isActivated(req.getIsActivated())
                    .build();
            rooms.add(room);
        }
        // 생성한 호실 한번에 저장
        return roomRepository.saveAll(rooms);
    }

    // 호실 정보 수정
    // 필터
    @Transactional
    public ResponseEntity<?> updateRoomSetting(CustomUserDetails customUserDetails, List<UpdateRoomSettingReq> updateRoomSettingReqs, String filterType) {
        List<Room> updatedRooms = new ArrayList<>();

        for (UpdateRoomSettingReq updateRoomSettingReq : updateRoomSettingReqs) {
            Long roomId = updateRoomSettingReq.getRoomId();
            Optional<Room> findRoom = roomRepository.findById(roomId);
            DefaultAssert.isTrue(findRoom.isPresent(), "호실 정보가 올바르지 않습니다.");
            Room room = findRoom.get();

            updateRoomAttribute(room, updateRoomSettingReq, filterType);
            updatedRooms.add(room);
        }

        // 필터타입에 따른 null 체크
        validateRoomAttributes(updatedRooms.get(0).getDormitory(), updatedRooms.get(0).getFloor(), filterType);

        // 수용인원 및 호실 개수 업데이트
        updateDormitorySize(updatedRooms);
        if ("ISACTIVATED".equals(filterType)) {
            updateRoomCount(updatedRooms); // 활성화 여부 변경 시에만 실행
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("호실 정보가 등록되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // Room 속성 업데이트
    private void updateRoomAttribute(Room room, UpdateRoomSettingReq updateRoomSettingReq, String filterType) {
        RoomType roomType = null;

        switch (filterType) {
            case "GENDER":
                if (room.getRoomType() == null) {
                    // 처음 등록: RoomType만 갱신하고, Dormitory와 연결하지 않음
                    roomType = roomTypeRepository.findTop1ByGender(updateRoomSettingReq.getGender());
                    room.updateRoomType(roomType);
                } else {
                    // 수정: roomSize와 gender로 RoomType을 찾고, DormitoryRoomType을 업데이트
                    roomType = roomTypeRepository.findByRoomSizeAndGender(room.getRoomType().getRoomSize(), updateRoomSettingReq.getGender());
                    room.updateRoomType(roomType);

                    // RoomType이 업데이트된 경우만 Dormitory와 연결 처리
                    updateDormitoryRoomTypes(room.getDormitory(), roomType);
                }
                break;

            case "ROOMSIZE":
                if (room.getRoomType() == null) {
                    // 처음 등록: RoomType만 갱신하고, Dormitory와 연결하지 않음
                    roomType = roomTypeRepository.findTop1ByRoomSize(updateRoomSettingReq.getRoomSize());
                    room.updateRoomType(roomType);

                } else {
                    // 수정: roomSize와 gender로 RoomType을 찾고, DormitoryRoomType을 업데이트
                    roomType = roomTypeRepository.findByRoomSizeAndGender(updateRoomSettingReq.getRoomSize(), room.getRoomType().getGender());
                    room.updateRoomType(roomType);

                    // RoomType이 업데이트된 경우 Dormitory와 연결 처리
                    updateDormitoryRoomTypes(room.getDormitory(), roomType);
                }
                break;

            case "HASKEY":
                room.updateHasKey(updateRoomSettingReq.getHasKey());
                break;

            case "ISACTIVATED":
                room.updateIsActivated(updateRoomSettingReq.getIsActivated());
                updateDormitoryRoomTypes(room.getDormitory(), room.getRoomType());
                break;

            default:
                throw new IllegalArgumentException("잘못된 filterType입니다.");
        }
    }

    // Dormitory와 RoomType 연결 및 제거 로직
    private void updateDormitoryRoomTypes(Dormitory dormitory, RoomType newRoomType) {
        // 기존 연결된 RoomType들을 가져옴
        List<RoomType> currentRoomTypes = dormitoryRoomTypeRepository.findByDormitory(dormitory).stream()
                .map(DormitoryRoomType::getRoomType)
                .toList();

        // 새로운 RoomType을 Dormitory에 추가
        if (!currentRoomTypes.contains(newRoomType)) {
            dormitoryRoomTypeRepository.save(
                    DormitoryRoomType.builder()
                            .dormitory(dormitory)
                            .roomType(newRoomType)
                            .build()
            );
        }

        // 현재 활성화된 방들의 RoomType을 기준으로 연결되지 않은 RoomType 제거
        List<RoomType> activeRoomTypes = roomRepository.findByDormitoryAndIsActivated(dormitory, true).stream()
                .map(Room::getRoomType)
                .distinct()
                .toList();

        for (RoomType roomType : currentRoomTypes) {
            if (!activeRoomTypes.contains(roomType)) {
                DormitoryRoomType dormitoryRoomType = dormitoryRoomTypeRepository.findByDormitoryAndRoomType(dormitory, roomType);
                dormitoryRoomTypeRepository.delete(dormitoryRoomType);
            }
        }
    }

    private void deleteDormitoryRoomTypes(Dormitory dormitory) {
        // 기존 연결된 RoomType들을 가져옴
        List<RoomType> currentRoomTypes = dormitoryRoomTypeRepository.findByDormitory(dormitory).stream()
                .map(DormitoryRoomType::getRoomType)
                .toList();

        // 현재 활성화된 방들의 RoomType을 기준으로 연결되지 않은 RoomType 제거
        List<RoomType> activeRoomTypes = roomRepository.findByDormitoryAndIsActivated(dormitory, true).stream()
                .map(Room::getRoomType)
                .distinct()
                .toList();

        for (RoomType roomType : currentRoomTypes) {
            if (!activeRoomTypes.contains(roomType)) {
                DormitoryRoomType dormitoryRoomType = dormitoryRoomTypeRepository.findByDormitoryAndRoomType(dormitory, roomType);
                dormitoryRoomTypeRepository.delete(dormitoryRoomType);
            }
        }
    }


    // 속성 값 확인 메서드
    private void validateRoomAttributes(Dormitory dormitory, Integer floor, String filterType) {
        boolean check = switch (filterType) {
            case "GENDER" -> !roomRepository.existsByDormitoryAndFloorAndRoomType_Gender(dormitory, floor, Gender.EMPTY);
            case "ROOMSIZE" -> !roomRepository.existsByDormitoryAndFloorAndRoomType_RoomSize(dormitory, floor, null);
            case "HASKEY" -> !roomRepository.existsByDormitoryAndFloorAndHasKey(dormitory, floor, null);
            case "ISACTIVATED" -> true; // 활성화 여부는 따로 확인하지 않음
            default -> throw new IllegalArgumentException("잘못된 filterType입니다.");
        };
        DefaultAssert.isTrue(check, "설정되지 않은 속성값이 있습니다.");
    }

    // 각 층별 호실의 null값 여부 확인
    private Boolean checkValidateRoom(Dormitory dormitory, Integer floor) {
        // 1. 해당 기숙사의 해당 층에 RoomType null인 방이 있는지 확인
        boolean hasInvalidRoomType = roomRepository.existsByDormitoryAndFloorAndRoomType(dormitory, floor, null);
        // 2. 해당 기숙사의 해당 층에 isActivated가 null인 방이 있는지 확인
        boolean hasInvalidIsActivated = roomRepository.existsByDormitoryAndFloorAndIsActivated(dormitory, floor, null);
        // 3. 해당 기숙사의 해당 층에 HasKey가 null인 방이 있는지 확인
        boolean hasInvalidHasKey = roomRepository.existsByDormitoryAndFloorAndHasKeyIsNull(dormitory, floor);
        // 위의 조건 중 하나라도 true이면 유효하지 않으므로 false 반환
        return !(hasInvalidRoomType || hasInvalidIsActivated || hasInvalidHasKey);
    }

    private void updateDormitorySize(List<Room> rooms) {
        Dormitory dormitory = rooms.get(0).getDormitory();
        List<RoomType> roomTypes = rooms.stream()
                .map(Room::getRoomType)
                .filter(Objects::nonNull)  // RoomType이 null인 경우 dormitorySize에 반영이 안 되어있으므로 제외
                .distinct()  // 중복 제거
                .toList();

        // 전체 기숙사 수용 인원 크기 누적 변수
        Integer totalDormitorySize = 0;
        for (RoomType roomType : roomTypes) {
            Integer roomSize = roomType.getRoomSize();
            Integer roomCount = roomRepository.countByDormitoryAndIsActivatedAndRoomType(dormitory, true, roomType);
            totalDormitorySize += (roomCount * roomSize);
            dormitory.updateDormitorySize(totalDormitorySize);
        }
    }

    private void updateRoomCount(List<Room> rooms) {
        Dormitory dormitory = rooms.get(0).getDormitory();
        Integer roomCount = roomRepository.countByDormitoryAndIsActivated(dormitory, true);
        dormitory.updateRoomCount(roomCount);
    }


    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }
}
