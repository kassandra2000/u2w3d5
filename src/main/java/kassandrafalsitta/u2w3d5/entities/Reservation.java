package kassandrafalsitta.u2w3d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private LocalDate dateReservation;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    //costruttore
    public Reservation( LocalDate dateReservation,  Event event, User user) {
        this.dateReservation = dateReservation;
        this.event = event;
        this.user = user;
    }
}
