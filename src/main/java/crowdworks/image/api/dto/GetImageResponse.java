package crowdworks.image.api.dto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetImageResponse(
        String url,

        String name,

        Double size,

        LocalDateTime createdAt) {
}
