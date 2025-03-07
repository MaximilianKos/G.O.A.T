package ch.kos.goat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.kos.goat.dto.bookmarks.BookmarkRequest;
import ch.kos.goat.services.BookmarkService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/bookmarks")
@AllArgsConstructor
@CrossOrigin
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @PostMapping
  public ResponseEntity<?> createBookmark(@RequestBody BookmarkRequest request) {
    try {
      return ResponseEntity.ok(bookmarkService.createBookmark(request));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<?> getBookmarks() {
    try {
      return ResponseEntity.ok(bookmarkService.getBookmarks());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateBookmark(@RequestBody BookmarkRequest request, @PathVariable Long id) {
    try {
      return ResponseEntity.ok(bookmarkService.updateBookmark(request, id));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
