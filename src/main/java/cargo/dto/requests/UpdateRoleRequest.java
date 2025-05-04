package cargo.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateRoleRequest (
        @NotBlank(message = "Role name cannot be blank")
        String name,
        String description,
        boolean status
) {
}
