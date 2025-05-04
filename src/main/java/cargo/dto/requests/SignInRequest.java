package cargo.dto.requests;

import cargo.validation.annotaion.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        @Email
        String login,
        @PasswordValidation
        String password
) {

}
