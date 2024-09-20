package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotEmpty;

public record StateEventDTO(
        @NotEmpty(message = "Lo stato è obbligatorio")
        String stateEvent
) {
}
