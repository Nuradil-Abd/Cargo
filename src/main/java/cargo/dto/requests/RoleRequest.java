package cargo.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest (
        @NotBlank(message = "Role name cannot be blank")
        String name,
        String description
){
}
