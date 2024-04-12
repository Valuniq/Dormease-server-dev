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
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.error.DefaultException;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitorySettingDetailService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final DormitoryRepository dormitoryRepository;

    // 호실 개수 추가
    @Transactional
    public ResponseEntity<?> addFloorAndRoomNumber(CustomUserDetails customUserDetails, Long dormitoryId, AddRoomNumberReq addRoomNumberReq) {
        User user = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "사용자가 존재하지 않습니다."));

        Dormitory dormitory = dormitoryRepository.findById(dormitoryId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER, "건물이 존재하지 않습니다."));

        isSameSchoolAsDormitory(user.getSchool(), dormitory.getSchool());

        Integer floor = addRoomNumberReq.getFloor();
        Integer start = addRoomNumberReq.getStartRoomNumber();
        Integer end = addRoomNumberReq.getEndRoomNumber();

        verifyRoomNumber(floor, start, end);

        try {
            List<Room> rooms = generateRoomNumbers(dormitory, floor, start, end);
            // RoomCount 업데이트
            dormitory.updateRoomCount(dormitory.getRoomCount() + rooms.size());
        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR, "호실 생성 중 오류가 발생했습니다.");
        }

        // 층으로 호실 조회메소드 호출
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("호실이 추가되었습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private List<Room> generateRoomNumbers(Dormitory dormitory, int floor, int startRoomNumber, int endRoomNumber) {
        List<Room> rooms = new ArrayList<>();
        try {
            for (int roomNumber = startRoomNumber; roomNumber <= endRoomNumber; roomNumber++) {
                int roomNumberInt = Integer.parseInt(floor + String.format("%02d", roomNumber));
                Room room = Room.builder()
                        .dormitory(dormitory)
                        .gender(Gender.EMPTY)
                        .floor(floor)
                        .currentPeople(0)
                        .isActivated(true)
                        .roomNumber(roomNumberInt)
                        .hasKey(false)
                        .build();
                rooms.add(room);
            }
        } catch (NumberFormatException e) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "호실 번호 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR, "호실 저장 중 오류가 발생했습니다.");
        }
        // 생성한 호실 한번에 저장
        return roomRepository.saveAll(rooms);

    }

    private static void verifyRoomNumber(Integer floor, Integer start, Integer end) {
        // 입력 값 유효성 검사
        if (floor < 1) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "층수는 양수여야 합니다.");
        }
        if (start > 99 || end > 99 || start <= 0 || end <= 0) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "호실 번호는 1부터 99까지의 값이어야 합니다.");
        }
        if (start > end) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "시작 호실 번호는 끝 호실 번호보다 작거나 같아야 합니다.");
        }
    }

    private void isSameSchoolAsDormitory(School userSchool, School dormitorySchool) {
        if (!Objects.equals(userSchool, dormitorySchool)) {
            throw new DefaultException(ErrorCode.INVALID_CHECK, "학교가 일치하지 않습니다");
        }
    }


}
