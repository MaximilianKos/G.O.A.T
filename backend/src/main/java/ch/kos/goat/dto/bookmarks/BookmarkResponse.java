package ch.kos.goat.dto.bookmarks;

import java.util.Set;

import ch.kos.goat.dto.DateAudit;
import ch.kos.goat.entities.Tag;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BookmarkResponse extends DateAudit {

  private Long id;
  private String title;
  private String url;
  private String thumbnail_url;
  private int clicks;
  private boolean archived;
  private Set<Tag> tags;
}
