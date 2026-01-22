package ch.kos.goat.services;

import java.util.List;
import java.util.Set;

import ch.kos.goat.enums.Type;
import ch.kos.goat.mapper.MomentMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.kos.goat.dto.moment.MomentRequest;
import ch.kos.goat.dto.moment.MomentResponse;
import ch.kos.goat.entities.Moment;
import ch.kos.goat.entities.Tag;
import ch.kos.goat.repositories.MomentRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class MomentService {

    private final MomentRepository momentRepository;
    private final TagService tagService;
    private final MomentMapper momentMapper;
    private final StorageService storageService;
    private YtDlpService ytDlpService;

    public MomentResponse createMoment(MomentRequest request, MultipartFile file) {
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        Moment entity = momentMapper.toEntity(request, tags);

        if (file != null && !file.isEmpty()) {
            String path = storageService.store(file);
            entity.setLocalPath(path);
        }

        Moment saved = momentRepository.save(entity);

        if (file == null && request.getSourceUrl() != null && !request.getSourceUrl().isEmpty() && (request.getType() == Type.VIDEO || request.getSourceUrl().contains("youtube.com") || request.getSourceUrl().contains("youtu.be"))) {
            ytDlpService.downloadVideo(saved.getId(), request.getSourceUrl());
        }

        return momentMapper.toMomentResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MomentResponse> getMoments() {
        return momentRepository.findAll().stream().map(momentMapper::toMomentResponse).toList();
    }

    public MomentResponse getMoment(Long id) {
        return momentMapper.toMomentResponse(getMomentOrThrow(id));
    }

    @Transactional
    public MomentResponse updateMoment(MomentRequest request, MultipartFile file, Long id) {
        Moment moment = getMomentOrThrow(id);

        momentMapper.updateMomentFromRequest(request, moment);

        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        moment.setTags(tags);

        if (file != null && !file.isEmpty()) {
            String path = storageService.store(file);
            moment.setLocalPath(path);
            moment.setArchived(true);
        }

        Moment updated = momentRepository.save(moment);
        return momentMapper.toMomentResponse(updated);
    }

    public void deleteMoment(Long id) {
        Moment moment = momentRepository.findById(id).orElseThrow(() -> new RuntimeException("Moment not found"));
        moment.setArchived(true);
        momentRepository.save(moment);
    }

    private Moment getMomentOrThrow(Long id) {
        return momentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Moment not found with id: " + id));
    }
}
