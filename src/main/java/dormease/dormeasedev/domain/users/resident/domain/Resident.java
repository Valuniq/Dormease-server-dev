package dormease.dormeasedev.domain.users.resident.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.Gender;
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
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_term_id")
    private DormitoryTerm dormitoryTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 침대 번호
    private Integer bedNumber;

    // 룸메이트 신청 여부
    private Boolean isRoommateApplied;

    // 열쇠 수령 여부
    private Boolean hasKey;

    public void updateHasKey(Boolean hasKey) {
        this.hasKey = hasKey;
    }

    @Builder
    public Resident(School school, Student student, DormitoryTerm dormitoryTerm, Room room, String name, Gender gender, Integer bedNumber, Boolean isRoommateApplied, Boolean hasKey) {
        this.school = school;
        this.student = student;
        this.dormitoryTerm = dormitoryTerm;
        this.room = room;
        this.name = name;
        this.gender = gender;
        this.bedNumber = bedNumber;
        this.isRoommateApplied = isRoommateApplied;
        this.hasKey = hasKey;
    }

    public void updateRoom(Room room) { this.room = room; }
    public void updateBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }
    public void updateDormitoryTerm(DormitoryTerm dormitoryTerm) { this.dormitoryTerm = dormitoryTerm; }

}
