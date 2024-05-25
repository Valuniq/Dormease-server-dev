package dormease.dormeasedev.domain.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotOrAssignedResidentsRes {

    private List<AssignedResidentRes> assignedResidentResList;

    private List<NotAssignedResidentRes> notAssignedResidentResList;

    @Builder
    public NotOrAssignedResidentsRes(List<AssignedResidentRes> assignedResidentResList, List<NotAssignedResidentRes> notAssignedResidentResList) {
        this.assignedResidentResList = assignedResidentResList;
        this.notAssignedResidentResList = notAssignedResidentResList;
    }

}
