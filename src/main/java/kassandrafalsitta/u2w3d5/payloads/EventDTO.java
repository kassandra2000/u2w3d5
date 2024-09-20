package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EventDTO(
        @NotEmpty(message = "La destinazione è obbligatoria")
        @Size(min = 3, max = 30, message = "La destinazione deve essere compresa tra 3 e 30 caratteri")
        String destination,
        @NotEmpty(message = "Il titolo è obbligatorio")
        @Size(min = 3, max = 30, message = "Il titolo deve essere compreso tra 3 e 30 caratteri")
        String title,
        @NotEmpty(message = "La descrizione è obbligatoria")
        @Size(min = 3, max = 30, message = "La descrizione deve essere compresa tra 3 e 30 caratteri")
        String description,
        @NotEmpty(message = "La data è obbligatoria")
        @Size(min = 10, max = 10, message = "La data deve avere 10 caratteri")
        String dateEvent,
        @NotEmpty(message = "Il luogo è obbligatorio")
        @Size(min = 3, max = 30, message = "Il luogo deve essere compreso tra 3 e 30 caratteri")
        String place,
        @NotEmpty(message = "Il numero di posti disponibili è obbligatorio")
        @Size(min = 1, max = 10, message = "Il numero di posti disponibili deve essere compreso tra 1 e 10 caratteri")
        String numberOfPlaces
) {
}
