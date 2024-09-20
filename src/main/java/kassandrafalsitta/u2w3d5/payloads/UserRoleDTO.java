package kassandrafalsitta.u2w3d5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRoleDTO(
        @NotEmpty(message = "Il ruolo è obbligatorio")
        @Size(min = 3, max = 15, message = "Il ruolo deve essere compreso tra 3 e 15 caratteri")
        String role
) {
}
