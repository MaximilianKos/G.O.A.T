package ch.kos.goat.mapper;

import ch.kos.goat.dto.tags.TagResponse;
import ch.kos.goat.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponse toTagResponse(Tag tag);
}
