package dormease.dormeasedev.domain.notification.dto.request;

import dormease.dormeasedev.domain.block.dto.request.BlockReq;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class WriteNotificataionReq {

    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항 or FAQ 제목")
    private String title;

    @Schema(type = "Boolean", example = "true", description = "상단핀 고정 체크 여부")
    private Boolean pinned;

    @Schema(type = "NotificationType", example = "FAQ", description = "ANNOUNCEMENT(공지사항) / FAQ 中 1")
    private NotificationType notificationType;

    @Schema(type = "BlockReq", description = "내용이 될 Block 리스트입니다.")
    List<BlockReq> blockReqList = new ArrayList<>();

}
