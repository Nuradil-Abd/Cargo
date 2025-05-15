package cargo.dto.requests;

import cargo.enums.Country;

public record CreateCompanyRequest(
        String name,
        String tin,
        String address,
        String email,
        String activityType,
        Country country,
        String region,
        boolean status
) {
}
