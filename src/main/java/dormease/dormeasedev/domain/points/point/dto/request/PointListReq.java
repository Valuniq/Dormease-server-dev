package dormease.dormeasedev.domain.points.point.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PointListReq {

    private List<BonusPointManagementReq> bonusPointList;

    private List<MinusPointManagementReq> minusPointList;

}
