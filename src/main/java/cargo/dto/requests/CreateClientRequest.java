package cargo.dto.requests;

import cargo.enums.Country;

public record CreateClientRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String login,
        String password,
        boolean acStatus,
        Country country,
        String activityType,
        String address
){
}
