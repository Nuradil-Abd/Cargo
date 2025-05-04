package cargo.dto.requests;

public record RoleDeactivationRequest (
        String roleName,
        String adminPassword
){
}
