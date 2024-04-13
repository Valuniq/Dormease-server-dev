package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.RoomSettingReq;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.AddRoomNumberReq;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.Gender;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.error.DefaultException;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.ErrorCode;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitorySettingDetailService {

    private final RoomRepository roomRepository;
    private final DormitoryRepository dormitoryRepository;

    // 호실 개수 추가
    @Transactional
    public ResponseEntity<?> addFloorAndRoomNumber(CustomUserDetails customUserDetails, Long dormitoryId, AddRoomNumberReq addRoomNumberReq) {

        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        Dormitory dormitory =findDormitory.get();

        Integer floor = addRoomNumberReq.getFloor();
        Integer start = addRoomNumberReq.getStartRoomNumber();
        Integer end = addRoomNumberReq.getEndRoomNumber();

        // 이미 해당 층이 존재하면 예외처리
        DefaultAssert.isTrue(roomRepository.findByDormitoryAndFloor(dormitory, floor).isEmpty(), "중복된 층이 존재합니다.");

        // 유효성 검사
        verifyRoomNumber(floor, start, end);

        List<Room> rooms = generateRoomNumbers(dormitory, floor, start, end);
        DefaultAssert.isTrue(!rooms.isEmpty(), "호실 생성 중 오류가 발생했습니다.");

        // 층으로 호실 조회메소드 호출
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("호실이 추가되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    private List<Room> generateRoomNumbers(Dormitory dormitory, int floor, int startRoomNumber, int endRoomNumber) {
        List<Room> rooms = new ArrayList<>();
        for (int roomNumber = startRoomNumber; roomNumber <= endRoomNumber; roomNumber++) {
            int roomNumberInt = Integer.parseInt(floor + String.format("%02d", roomNumber));
            Room room = Room.builder()
                    .dormitory(dormitory)
                    .gender(Gender.EMPTY)
                    .floor(floor)
                    .currentPeople(0)
                    .roomNumber(roomNumberInt)
                    .build();
            rooms.add(room);
        }
        // 생성한 호실 한번에 저장
        return roomRepository.saveAll(rooms);

    }

    private static void verifyRoomNumber(Integer floor, Integer start, Integer end) {
        // 입력 값 유효성 검사
        DefaultAssert.isTrue(floor < 1, "층수는 양수여야 합니다.");
        DefaultAssert.isTrue(start > 99 || end > 99 || start <= 0 || end <= 0, "호실 번호는 1부터 99까지의 값이어야 합니다.");
        DefaultAssert.isTrue(start > end, "시작 호실 번호는 끝 호실 번호보다 작거나 같아야 합니다.");
    }

    // 건물, 층으로 호실 조회
    public ResponseEntity<?> getRoomsByDormitoryAndFloor(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {

        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        Dormitory dormitory =findDormitory.get();


        // dormitory랑 floor로 조회
        // 이름이 같은 dormitory의 floor 다 가져와서
        // 호실 번호 순으로 정렬
        return ResponseEntity.ok("2");
    }

    // 호실 삭제

    // 층 수, 방 개수 수정


    // 호실 정보 수정
    // 필터
    @Transactional
    public ResponseEntity<?> updateRoomSetting(CustomUserDetails customUserDetails, List<RoomSettingReq> roomSettingReqs) {

        for (RoomSettingReq roomSettingReq : roomSettingReqs) {
            Long roomId = roomSettingReq.getId();
            Optional<Room> findRoom = roomRepository.findById(roomId);
            DefaultAssert.isTrue(findRoom.isPresent(), "호실 정보가 올바르지 않습니다.");
            Room room = findRoom.get();

            // 입력 유효성 검사
            validateRoomSetting(room, roomSettingReq);

            // 기존 dormitory의 Gender, roomSize
            String existingGender = room.getDormitory().getGender().toString();
            Integer existingRoomSize = room.getDormitory().getRoomSize();

            // 기존 dormitory와 다른 Gender 또는 roomSize인 경우
            if (!Objects.equals(existingGender, roomSettingReq.getGender()) || !Objects.equals(existingRoomSize, roomSettingReq.getRoomSize())) {
                createOrUpdateDormitory(room, roomSettingReq);
            }

            // 적절한 dormitory에 room 할당
            assignRoomToDormitory(room, roomSettingReq);

        }
        // 조회 호출
        return null;
    }

    private void createOrUpdateDormitory(Room room, RoomSettingReq roomSettingReq){
        Dormitory dormitory = room.getDormitory();
        // 기존에 저장된 dormitory가 있는지
        Dormitory existDormitory = dormitoryRepository.findBySchoolAndNameAndGenderAndRoomSize(dormitory.getSchool(), dormitory.getName(), Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getRoomSize());
        if (existDormitory == null) {
            // EMPTY면 기존 정보 업데이트
            if (dormitory.getGender() == Gender.EMPTY) {
                dormitory.updateGenderAndRoomSize(Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getRoomSize());
            } else {
                Gender gender = Gender.valueOf(roomSettingReq.getGender());
                Integer roomSize = roomSettingReq.getRoomSize();
                // 새로운 dormitory 생성
                Dormitory newDormitory = Dormitory.builder()
                        .school(dormitory.getSchool())
                        .name(dormitory.getName())
                        .memo(dormitory.getMemo())
                        .gender(gender)
                        .roomSize(roomSize)
                        .imageUrl(dormitory.getImageUrl())
                        .dormitorySize(dormitory.getDormitorySize())
                        .roomCount(0)
                        .build();

                dormitoryRepository.save(newDormitory);
        }
        }

    }

    private void assignRoomToDormitory(Room room, RoomSettingReq roomSettingReq) {
        Dormitory dormitory = room.getDormitory();
        // 이름,  GENDER, roomSize 같은 기숙사 조회
        Dormitory newDormitory = dormitoryRepository.findBySchoolAndNameAndGenderAndRoomSize(dormitory.getSchool(), dormitory.getName(), Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getRoomSize());
        DefaultAssert.isTrue(newDormitory != null, "해당하는 건물이 없습니다.");
        // room update
        room.updateRoomSetting(newDormitory, roomSettingReq.getRoomSize(), Gender.valueOf(roomSettingReq.getGender()), roomSettingReq.getHasKey());
        if (roomSettingReq.getIsActivated() != null) { room.updateIsActivated(roomSettingReq.getIsActivated());}

    }

    private void validateRoomSetting(Room room, RoomSettingReq roomSettingReq){
        DefaultAssert.isTrue(room.getId().equals(roomSettingReq.getId()), "호실이 일치하지 않습니다.");

        Gender gender = Gender.valueOf(roomSettingReq.getGender());
        DefaultAssert.isTrue(gender == Gender.MALE || gender == Gender.FEMALE, "호실의 성별이 지정되어있지 않습니다.");
    }
}
