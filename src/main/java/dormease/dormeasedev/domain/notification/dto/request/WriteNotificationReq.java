package dormease.dormeasedev.domain.notification.dto.request;

import dormease.dormeasedev.domain.image.dto.request.ImageReq;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class WriteNotificationReq {

    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항(FAQ) 제목")
    private String title;

    @Schema(type = "Boolean", example = "true", description = "상단핀 고정 체크 여부")
    private Boolean pinned;

    @Schema(type = "NotificationType", example = "FAQ", description = "ANNOUNCEMENT(공지사항) / FAQ 中 1")
    private NotificationType notificationType;

    @Schema(type = "String", example = "html로 변환된 내용", description = "공지사항(FAQ) 내용")
    private String content;

    @Schema(type = "ImageReq", description = "텍스트 에디터 속 들어갈 이미지 리스트")
    private List<ImageReq> imageReqList = new ArrayList<>();

}
