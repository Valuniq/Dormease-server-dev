package dormease.dormeasedev.domain.notifications_requestments.notification.dto.request;

import dormease.dormeasedev.domain.notifications_requestments.image.dto.request.ImageReq;
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

    @Schema(type = "String", example = "html로 변환된 내용", description = "수정할 공지사항(FAQ) 내용")
    private String content;

    @Schema(type = "List<Long>", example = "[1, 2, 3]", description = "삭제할 (텍스트 에디터 속)이미지 리스트. 없을 시 null")
    private List<Long> deletedImageIds = new ArrayList<>();

    @Schema(type = "ImageReq", description = "텍스트 에디터 속 추가로 들어갈 이미지 리스트")
    private List<ImageReq> imageReqList = new ArrayList<>();

    @Schema(type = "List<Long>", example = "[1, 2, 3]", description = "삭제할 파일 리스트. 없을 시 null")
    private List<Long> deletedFileIds = new ArrayList<>();

}
