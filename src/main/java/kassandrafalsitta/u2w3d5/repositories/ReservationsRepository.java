package kassandrafalsitta.u2w3d5.repositories;

import kassandrafalsitta.u2w3d5.entities.Reservation;
import kassandrafalsitta.u2w3d5.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservation, UUID> {
    //anche se non ho un parametro chiamato UserId Spring si va a cercare l'id del dipendente perchè tramite il nome capisce che sto cercando il campo id
    //uso il trattino _ per convenzione (perchè dateEvent è un parametro della classe event)
    Optional<Reservation> findByUserIdAndEvent_DateEvent(UUID userId, LocalDate dateEvent);

    List<Reservation> findByUser(User user);
}
