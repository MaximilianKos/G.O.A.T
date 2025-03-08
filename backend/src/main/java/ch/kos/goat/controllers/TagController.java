package ch.kos.goat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.kos.goat.dto.tags.TagRequest;
import ch.kos.goat.services.TagService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
@CrossOrigin
public class TagController {

  private final TagService tagService;

  @PostMapping
  public ResponseEntity<?> createTag(@RequestBody TagRequest request) {
    try {
      return ResponseEntity.ok(tagService.createTag(request));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<?> getTags() {
    try {
      return ResponseEntity.ok(tagService.getTags());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTag(@PathVariable Long id) {
    try {
      tagService.deleteTag(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
