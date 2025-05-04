package cargo.dto.reponces;

import lombok.Builder;

@Builder
public record GetRoleResponse(
        String roleName,
        String description,
        Long usersCount,
        boolean status
) {
}
