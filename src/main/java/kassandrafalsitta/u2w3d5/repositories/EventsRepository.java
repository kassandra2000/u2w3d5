package kassandrafalsitta.u2w3d5.repositories;

import kassandrafalsitta.u2w3d5.entities.Event;
import kassandrafalsitta.u2w3d5.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository  extends JpaRepository<Event, UUID> {
    Optional<Event> findByDateEventAndPlace(LocalDate dateEvent, String place);

}
