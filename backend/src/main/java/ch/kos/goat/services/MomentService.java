package ch.kos.goat.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.kos.goat.dto.moment.MomentRequest;
import ch.kos.goat.dto.moment.MomentResponse;
import ch.kos.goat.entities.Moment;
import ch.kos.goat.entities.Tag;
import ch.kos.goat.repositories.MomentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class MomentService {

    private final MomentRepository momentRepository;
    private final TagService tagService; // assumed to exist to resolve tag IDs

    private MomentResponse toMomentResponse(Moment moment) {
        return MomentResponse.builder()
            .id(moment.getId())
            .title(moment.getTitle())
            .sourceUrl(moment.getSourceUrl())
            .description(moment.getDescription())
            .momentAt(moment.getMomentAt())
            .category(moment.getCategory())
            .type(moment.getType())
            .thumbnailUrl(moment.getThumbnailUrl())
            .localPath(moment.getLocalPath())
            .archivedAt(moment.getArchivedAt())
            .clicks(moment.getClicks())
            .archived(moment.isArchived())
            .tags(moment.getTags())
            .createdAt(moment.getCreatedAt())
            .updatedAt(moment.getUpdatedAt())
            .build();
    }

    public MomentResponse createMoment(MomentRequest request) {
        try {
            Set<Tag> tags = tagService.getTagsByIds(request.getTagIds()); // implement in TagService

            Moment moment = Moment.builder()
                .title(request.getTitle())
                .sourceUrl(request.getSourceUrl())
                .description(request.getDescription())
                .momentAt(request.getMomentAt())
                .category(request.getCategory())
                .type(request.getType())
                .thumbnailUrl(request.getThumbnailUrl())
                .tags(tags)
                .build();

            Moment saved = momentRepository.save(moment);
            return toMomentResponse(saved);

        } catch (Exception e) {
            throw new RuntimeException("Error creating moment", e);
        }
    }

    public List<MomentResponse> getMoments() {
        try {
            return momentRepository.findAll().stream().map(this::toMomentResponse).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error getting moments", e);
        }
    }

    public MomentResponse updateMoment(MomentRequest request, Long id) {
        try {
            Moment moment = momentRepository.findById(id).orElseThrow(() -> new RuntimeException("Moment not found"));

            Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());

            moment.setTitle(request.getTitle());
            moment.setSourceUrl(request.getSourceUrl());
            moment.setDescription(request.getDescription());
            moment.setMomentAt(request.getMomentAt());
            moment.setCategory(request.getCategory());
            moment.setType(request.getType());
            moment.setThumbnailUrl(request.getThumbnailUrl());
            moment.setTags(tags);

            Moment updated = momentRepository.save(moment);
            return toMomentResponse(updated);

        } catch (Exception e) {
            throw new RuntimeException("Error updating moment", e);
        }
    }

    public void deleteMoment(Long id) {
        try {
            Moment moment = momentRepository.findById(id).orElseThrow(() -> new RuntimeException("Moment not found"));
            moment.setArchived(true);
            momentRepository.save(moment);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting moment", e);
        }
    }
}
