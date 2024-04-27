package dormease.dormeasedev.domain.point.dto.request;

import jdk.dynalink.linker.LinkerServices;
import lombok.Getter;

import java.util.List;

@Getter
public class PointListReq {

    private List<BonusPointManagementReq> bonusPointList;

    private List<MinusPointManagementReq> minusPointList;

}
