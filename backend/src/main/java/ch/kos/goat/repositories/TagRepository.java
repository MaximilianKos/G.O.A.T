package ch.kos.goat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.kos.goat.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
