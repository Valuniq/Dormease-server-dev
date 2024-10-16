package dormease.dormeasedev.domain.users.resident.dto;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryAndRoomType {

    private Dormitory dormitory;
    private RoomType roomType;

    public DormitoryAndRoomType(Dormitory dormitory, RoomType roomType) {
        this.dormitory = dormitory;
        this.roomType = roomType;
    }
}
