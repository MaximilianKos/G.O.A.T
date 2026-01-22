package ch.kos.goat.repositories;

import ch.kos.goat.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
