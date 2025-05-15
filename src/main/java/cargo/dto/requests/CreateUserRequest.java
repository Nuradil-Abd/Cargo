package cargo.dto.requests;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String login,
        String password,
        boolean active,
        Long companyId,
        String role
) {


}
