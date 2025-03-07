package ch.kos.goat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.kos.goat.entities.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
