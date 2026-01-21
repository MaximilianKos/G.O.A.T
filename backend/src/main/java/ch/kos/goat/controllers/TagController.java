package ch.kos.goat.controllers;

import ch.kos.goat.dto.tags.TagResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
@CrossOrigin
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags() {
        return ResponseEntity.ok(tagService.getTags());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}
