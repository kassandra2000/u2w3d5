package kassandrafalsitta.u2w3d5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String title;
    private String description;
    private LocalDate dateEvent;
    private String place;
    private int numberOfPlaces;
    //costruttore
    public Event(String title, String description, LocalDate dateEvent, String place, int numberOfPlaces) {
        this.title = title;
        this.description = description;
        this.dateEvent = dateEvent;
        this.place = place;
        this.numberOfPlaces = numberOfPlaces;
    }
}
