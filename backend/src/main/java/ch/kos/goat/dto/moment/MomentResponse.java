package ch.kos.goat.dto.moment;

import java.time.LocalDateTime;
import java.util.Set;

import ch.kos.goat.entities.Tag;

import ch.kos.goat.enums.Type;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomentResponse {

    private Long id;
    private String title;
    private String sourceUrl;
    private String description;
    private Type type;
    private String thumbnailUrl;
    private String localPath;
    private LocalDateTime archivedAt;
    private int clicks;
    private boolean archived;

    private Set<Tag> tags;
}
