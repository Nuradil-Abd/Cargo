package cargo.dto.reponces;

import cargo.enums.Country;

public record GetClientResponse(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        boolean active,
        Country country,
        String activityType,
        String address,
        String login
) {

}
