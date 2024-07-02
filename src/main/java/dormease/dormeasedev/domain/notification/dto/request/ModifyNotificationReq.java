package dormease.dormeasedev.domain.notification.dto.request;

import dormease.dormeasedev.domain.block.dto.request.BlockReq;
import dormease.dormeasedev.domain.block.dto.request.UpdateBlockReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ModifyNotificationReq {

    @Schema(type = "Long", example = "1", description = "공지사항(FAQ) ID")
    private Long notificationId;

    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항(FAQ) 제목")
    private String title;

    @Schema(type = "Boolean", example = "true", description = "상단핀 고정 체크 여부")
    private Boolean pinned;

    @Schema(type = "List<Long>", example = "[1, 2, 3]", description = "삭제할 블럭 리스트. 없을 시 null")
    private List<Long> deletedBlockIds = new ArrayList<>();

    @Schema(type = "BlockReq", description = "새로 추가할 Block List입니다.")
    private List<BlockReq> createBlockReqList = new ArrayList<>();

    @Schema(type = "BlockReq", description = "수정할 Block List입니다.")
    private List<UpdateBlockReq> updateBlockReqList = new ArrayList<>();

    @Schema(type = "List<Long>", example = "[1, 2, 3]", description = "삭제할 파일 리스트. 없을 시 null")
    private List<Long> deletedFileIds = new ArrayList<>();

}
