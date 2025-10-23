package ch.kos.goat.dto.moment;

import java.time.LocalDateTime;
import java.util.Set;

import ch.kos.goat.dto.DateAudit;
import ch.kos.goat.entities.Tag;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MomentResponse extends DateAudit {

    private Long id;
    private String title;
    private String sourceUrl;
    private String description;
    private LocalDateTime momentAt;
    private String category;
    private String type;
    private String thumbnailUrl;
    private String localPath;
    private LocalDateTime archivedAt;
    private int clicks;
    private boolean archived;

    private Set<Tag> tags;
}
