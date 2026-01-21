package ch.kos.goat.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.kos.goat.mapper.TagMapper;
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
    private final TagMapper tagMapper;

    public TagResponse createTag(TagRequest request) {
        Tag tag = Tag.builder().name(request.getName()).color(request.getColor()).build();
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tagMapper::toTagResponse).toList();
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public Set<Tag> getTagsByIds(List<Long> tagIds) {
        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id)))
                .collect(Collectors.toSet());
    }
}
