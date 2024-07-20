package dormease.dormeasedev.domain.image.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ImageRes {

    private Long imageId;

    private String imageUrl;
}
