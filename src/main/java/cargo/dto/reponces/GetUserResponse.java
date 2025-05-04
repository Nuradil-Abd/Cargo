package cargo.dto.reponces;


import cargo.entity.User;
import lombok.Builder;

@Builder
public record GetUserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String role,
        Boolean accountActive,
        String login
) {
    public static GetUserResponse from(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getName())
                .accountActive(user.getAccount() != null ? user.getAccount().isActive() : null)
                .login(user.getAccount() != null ? user.getAccount().getLogin() : null)
                .build();
    }
}
