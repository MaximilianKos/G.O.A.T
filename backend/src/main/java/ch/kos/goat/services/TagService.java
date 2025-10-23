package ch.kos.goat.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.kos.goat.dto.tags.TagRequest;
import ch.kos.goat.dto.tags.TagResponse;
import ch.kos.goat.entities.Tag;
import ch.kos.goat.repositories.TagRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  private TagResponse toTagResponse(Tag tag) {
    return TagResponse.builder()
        .id(tag.getId())
        .name(tag.getName())
        .color(tag.getColor())
        .createdAt(tag.getCreatedAt())
        .updatedAt(tag.getUpdatedAt())
        .build();
  }

  public TagResponse createTag(TagRequest request) {
    try {
      Tag tag = Tag.builder().name(request.getName()).color(request.getColor()).build();
      tagRepository.save(tag);
      return toTagResponse(tag);
    } catch (Exception e) {
      throw new RuntimeException("Error creating tag");
    }
  }

  public List<TagResponse> getTags() {
    try {
      List<Tag> tags = tagRepository.findAll();
      return tags.stream().map(this::toTagResponse).collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Error getting tags");
    }
  }

  public void deleteTag(Long id) {
    try {
      tagRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Error deleting tag");
    }
  }

    public Set<Tag> getTagsByIds(List<Long> tagIds) {
        try {
            return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id)))
                .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching tags by IDs", e);
        }
    }
}
