package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReservationRespDTO(
        @NotNull(message = "L'UUID è obbligatorio")
        UUID reservationId) {
}
