package dormease.dormeasedev.global.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ToString
@Getter
public class PageInfo {

    @Schema(type = "Integer", example = "0", description = "현재 페이지. 0부터 시작. (요청한 페이지)")
    private Integer currentPage;

    @Schema(type = "Integer", example = "5", description = "전체 페이지 개수")
    private Integer totalPage;

    @Schema(type = "Integer", example = "13", description = "한 페이지의 사이즈")
    private Integer pageSize;

    @Schema(type = "Long", example = "33", description = "전체 요소 개수")
    private Long totalElements;

    public PageInfo(){};

    @Builder
    public PageInfo(Integer currentPage, Integer totalPage, Integer pageSize, Long totalElements) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    public static PageInfo toPageInfo(Pageable pageable, Page<?> pageContent) {
        return PageInfo.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(pageContent.getTotalPages())
                .pageSize(pageable.getPageSize())
                .totalElements(pageContent.getTotalElements())
                .build();
    }
}
