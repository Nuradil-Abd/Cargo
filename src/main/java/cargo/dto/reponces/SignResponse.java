package cargo.dto.reponces;


import cargo.entity.Role;
import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public record SignResponse(
    Long id,
    String token,
    String email,
    String login,
    Role role,
    String message,
    HttpStatus httpStatus
){
}
