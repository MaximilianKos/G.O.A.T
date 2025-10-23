package ch.kos.goat.dto.moment;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MomentRequest {

    private String title;
    private String sourceUrl;
    private String description;
    private LocalDateTime momentAt;
    private String category;
    private String type;
    private String thumbnailUrl;

    private List<Long> tagIds;
}
