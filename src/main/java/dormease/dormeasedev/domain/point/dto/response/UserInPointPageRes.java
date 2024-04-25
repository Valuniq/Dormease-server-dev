package dormease.dormeasedev.domain.point.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInPointPageRes {

    // user id
    private Long id;

    private String name;

    private String studentNumber;

    private String phoneNumber;

    private Integer bonusPoint;

    private Integer minusPoint;

    private String dormitory;

    private Integer room;

    @Builder
    public UserInPointPageRes(Long id, String name, String studentNumber, String phoneNumber, Integer bonusPoint, Integer minusPoint, String dormitory, Integer room) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.dormitory = dormitory;
        this.room = room;
    }

}
