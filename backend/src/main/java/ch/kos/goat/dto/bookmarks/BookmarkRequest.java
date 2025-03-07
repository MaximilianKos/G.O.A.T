package ch.kos.goat.dto.bookmarks;

import java.util.List;

import lombok.Data;

@Data
public class BookmarkRequest {

  private String title;
  private String url;
  private String thumbnail_url;
  private List<Long> tagIds;
}
