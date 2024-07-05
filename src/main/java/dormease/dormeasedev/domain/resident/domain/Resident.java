package dormease.dormeasedev.domain.resident.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.roommate_application.domain.RoommateApplication;
import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Resident extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roommate_temp_application_id")
    private RoommateTempApplication roommateTempApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roommate_application_id")
    private RoommateApplication roommateApplication;

    // 침대 번호
    private Integer bedNumber;

    // 룸메이트 신청 여부
    private Boolean isRoommateApplied;

    // 열쇠 수령 여부
    private Boolean hasKey;

    // update 함수
    public void updateRoommateTempApplication(RoommateTempApplication roommateTempApplication) {
        this.roommateTempApplication = roommateTempApplication;
    }

    public void addRoommateTempApplication(RoommateTempApplication roommateTempApplication) {
        this.roommateTempApplication = roommateTempApplication;
        if (roommateTempApplication.getResidents() == null)
            roommateTempApplication.updateResidents();
        roommateTempApplication.getResidents().add(this);
    }

    public void removeRoommateTempApplication(RoommateTempApplication roommateTempApplication) {
        this.roommateTempApplication = null;
        roommateTempApplication.getResidents().remove(this);
    }

    public void updateRoommateApplication(RoommateApplication roommateApplication) {
        this.roommateApplication = roommateApplication;
    }

    @Builder
    public Resident(School school, User user, Room room, RoommateTempApplication roommateTempApplication, RoommateApplication roommateApplication, Integer bedNumber, Boolean isRoommateApplied, Boolean hasKey) {
        this.school = school;
        this.user = user;
        this.room = room;
        this.roommateTempApplication = roommateTempApplication;
        this.roommateApplication = roommateApplication;
        this.bedNumber = bedNumber;
        this.isRoommateApplied = isRoommateApplied;
        this.hasKey = hasKey;
    }

    public void updateRoom(Room room) { this.room = room; }
    public void updateBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }
}
