package ch.kos.goat.repositories;

import ch.kos.goat.entities.Moment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MomentRepository extends JpaRepository<Moment, Long> {

}
