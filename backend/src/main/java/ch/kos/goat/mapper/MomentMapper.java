package ch.kos.goat.mapper;

import ch.kos.goat.dto.moment.MomentRequest;
import ch.kos.goat.dto.moment.MomentResponse;
import ch.kos.goat.entities.Moment;
import ch.kos.goat.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MomentMapper {

    MomentResponse toMomentResponse(Moment moment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "archived", constant = "false")
    @Mapping(target = "clicks", constant = "0")
    @Mapping(target = "localPath", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    Moment toEntity(MomentRequest request, Set<Tag> tags);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "clicks", ignore = true)
    @Mapping(target = "archived", ignore = true)
    void updateMomentFromRequest(MomentRequest req, @MappingTarget Moment moment);
}
