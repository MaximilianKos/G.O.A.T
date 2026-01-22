package ch.kos.goat.dto.moment;

import java.util.List;

import ch.kos.goat.enums.Type;
import lombok.Data;

@Data
public class MomentRequest {

    private String title;
    private String sourceUrl;
    private String description;
    private Type type;
    private String thumbnailUrl;

    private List<Long> tagIds;
}
