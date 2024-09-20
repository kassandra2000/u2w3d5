package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EventRespDTO(
        @NotNull(message = "L'UUID è obbligatorio")
        UUID eventId) {
}
