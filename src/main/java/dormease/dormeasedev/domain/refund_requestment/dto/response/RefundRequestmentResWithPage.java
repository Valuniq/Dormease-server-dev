package dormease.dormeasedev.domain.refund_requestment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefundRequestmentResWithPage {

    @Schema(type = "Integer", example = "13", description = "전체 페이지 개수")
    private Integer totalPage;

    @Schema(type = "List<RefundRequestmentRes>", example = "RefundRequestmentResList", description = "현재 페이지에서 불러올 환불 신청 사생 목록")
    List<RefundRequestmentRes> refundRequestmentResList = new ArrayList<>();

}
