package ch.kos.goat.entities;

import java.time.LocalDateTime;
import java.util.Set;

import ch.kos.goat.enums.Type;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "moments")
public class Moment extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "local_path")
    private String localPath;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Builder.Default
    @Column(nullable = false)
    private int clicks = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean archived = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "moment_tags", joinColumns = @JoinColumn(name = "moment_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;
}