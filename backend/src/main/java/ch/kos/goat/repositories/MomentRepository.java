package ch.kos.goat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.kos.goat.entities.Moment;

public interface MomentRepository extends JpaRepository<Moment, Long> {

}
