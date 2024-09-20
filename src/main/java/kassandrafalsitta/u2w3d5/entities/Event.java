package kassandrafalsitta.u2w3d5.entities;

import jakarta.persistence.*;
import kassandrafalsitta.u2w3d5.enums.StateEvent;
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
    @Enumerated(EnumType.STRING)
    private StateEvent stateEvent;
    private User userPost;

    //costruttore
    public Event(String title, String description, LocalDate dateEvent, String place, int numberOfPlaces, User userPost) {
        this.title = title;
        this.description = description;
        this.dateEvent = dateEvent;
        this.place = place;
        this.numberOfPlaces = numberOfPlaces;
        this.stateEvent = StateEvent.AVAILABLE;
        this.userPost = userPost;
    }
}
