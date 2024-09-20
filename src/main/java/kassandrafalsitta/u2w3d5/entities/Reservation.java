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
    @Setter(AccessLevel.NONE)
    private LocalDate dateReservation;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    //costruttore
    public Reservation(Event event, User user) {
        this.dateReservation = LocalDate.now();
        this.event = event;
        this.user = user;
    }
}
