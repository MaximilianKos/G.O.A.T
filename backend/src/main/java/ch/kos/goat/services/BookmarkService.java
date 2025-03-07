package ch.kos.goat.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.kos.goat.dto.bookmarks.BookmarkRequest;
import ch.kos.goat.dto.bookmarks.BookmarkResponse;
import ch.kos.goat.entities.Bookmark;
import ch.kos.goat.repositories.BookmarkRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private BookmarkResponse toBookmarkResponse(Bookmark bookmark) {
    return BookmarkResponse.builder()
        .id(bookmark.getId())
        .title(bookmark.getTitle())
        .url(bookmark.getUrl())
        .thumbnail_url(bookmark.getThumbnail_url())
        .clicks(bookmark.getClicks())
        .archived(bookmark.isArchived())
        .tags(bookmark.getTags())
        .createdAt(bookmark.getCreatedAt())
        .updatedAt(bookmark.getUpdatedAt())
        .build();
  }

  public BookmarkResponse createBookmark(BookmarkRequest request) {
    try {
      Bookmark bookmark = Bookmark.builder()
          .title(request.getTitle())
          .url(request.getUrl())
          .thumbnail_url(request.getThumbnail_url())
          // get tag ids from request and get them from TagService
          .tags(null)
          .build();
      bookmarkRepository.save(bookmark);
      return toBookmarkResponse(bookmark);
    } catch (Exception e) {
      throw new RuntimeException("Error creating bookmark");
    }
  }

  public List<BookmarkResponse> getBookmarks() {
    try {
      List<Bookmark> bookmarks = bookmarkRepository.findAll();
      return bookmarks.stream().map(this::toBookmarkResponse).collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Error getting bookmarks");
    }
  }

  public BookmarkResponse updateBookmark(BookmarkRequest request, Long id) {
    try {
      Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new RuntimeException("Bookmark not found"));
      bookmark.setTitle(request.getTitle());
      bookmark.setUrl(request.getUrl());
      bookmark.setThumbnail_url(request.getThumbnail_url());
      // get tag ids from request and get them from TagService
      bookmark.setTags(null);
      bookmarkRepository.save(bookmark);
      return toBookmarkResponse(bookmark);
    } catch (Exception e) {
      throw new RuntimeException("Error updating bookmark");
    }
  }
}
