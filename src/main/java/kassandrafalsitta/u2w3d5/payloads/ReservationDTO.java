package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ReservationDTO(
        @NotEmpty(message = "La data è obbligatoria")
        @Size(min = 10, max = 10, message = "La data deve avere 10 caratteri")
        String dateReservation,
        @NotEmpty(message = "L'UUID dell' evento è obbligatorio")
        @Size(min = 36, max = 36, message = "L'UUID dell' evento  deve avere 36 caratteri")
        String eventID,
        @NotEmpty(message = "L'UUID dell' utente è obbligatorio")
        @Size(min = 36, max = 36, message = "L'UUID dell' utente  deve avere 36 caratteri")
        String userID
) {
}
