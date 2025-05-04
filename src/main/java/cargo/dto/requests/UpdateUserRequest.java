package cargo.dto.requests;

import lombok.Builder;

@Builder
public record UpdateUserRequest (
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String roleName,
        Boolean accountActive,
        String login,
        String password
){
}
