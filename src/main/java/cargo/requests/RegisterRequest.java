package cargo.requests;


import cargo.entity.Role;
import cargo.validation.annotaion.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 3, max = 20, message = "First name must be more than 3 and less than 20 words")
        String firstName,
        @NotBlank(message = "Last name cannot be empty")
        @Size(min = 3, max = 20, message = "Last name must be more than 3 and less than 20 words")
        String lastName,
        @NotBlank(message = "Email cannot be empty")
        @Email(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$",
                message = "Invalid email format. Only end with .com")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @PasswordValidation
        String password,
        Role role
) {

}
