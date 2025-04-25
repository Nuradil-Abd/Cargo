package cargo.reponces;


import cargo.entity.Role;
import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public record SignUpResponse (
    Long id,
    String token,
    String email,
    Role role,
    String message,
    HttpStatus httpStatus
){
}
