package ch.kos.goat.services;

import java.util.List;
import java.util.Set;

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

@Service
@AllArgsConstructor
@Transactional
public class MomentService {

    private final MomentRepository momentRepository;
    private final TagService tagService;
    private final MomentMapper momentMapper;

    public MomentResponse createMoment(MomentRequest request) {
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        Moment saved = momentRepository.save(momentMapper.toEntity(request, tags));
        return momentMapper.toMomentResponse(saved);
    }

    public List<MomentResponse> getMoments() {
        return momentRepository.findAll().stream().map(momentMapper::toMomentResponse).toList();
    }

    public MomentResponse getMoment(Long id) {
        return momentMapper.toMomentResponse(getMomentOrThrow(id));
    }

    @Transactional
    public MomentResponse updateMoment(MomentRequest request, Long id) {
        Moment moment = getMomentOrThrow(id);
        momentMapper.updateMomentFromRequest(request, moment);
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        moment.setTags(tags);
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
