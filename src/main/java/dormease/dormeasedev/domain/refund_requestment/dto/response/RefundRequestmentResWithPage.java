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

    @Schema(type = "Integer", example = "5", description = "전체 페이지 개수")
    private Integer totalPage;

    @Schema(type = "Integer", example = "0", description = "현재 페이지. 0부터 시작. (요청한 페이지임)")
    private Integer currentPage;

    @Schema(type = "Integer", example = "13", description = "한 페이지의 사이즈")
    private Integer pageSize;

    @Schema(type = "List<RefundRequestmentRes>", example = "RefundRequestmentResList", description = "현재 페이지에서 불러올 환불 신청 사생 목록")
    List<RefundRequestmentRes> refundRequestmentResList = new ArrayList<>();

}
