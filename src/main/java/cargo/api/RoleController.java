package cargo.api;

import cargo.dto.reponces.GetRoleResponse;
import cargo.dto.requests.RoleDeactivationRequest;
import cargo.dto.requests.RoleRequest;
import cargo.dto.requests.UpdateRoleRequest;
import cargo.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/role")
@RestController
@RequiredArgsConstructor
@Secured("ADMIN")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request) {
        roleService.createRole(request);
        return ResponseEntity.ok("Role created successfully");
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<String> deactivateRole(@RequestBody RoleDeactivationRequest request) {
        roleService.deactivateRoleWithAdminPassword(request);
        return ResponseEntity.ok("Role successfully deactivated");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRole(
            @RequestParam String roleName,
            @RequestParam String adminPassword
    ) {
        roleService.deleteRole(roleName, adminPassword);
        return ResponseEntity.ok("Role deleted successfully!");
    }

    @PutMapping("/{roleName}")
    public ResponseEntity<String> updateRole(@PathVariable String roleName,
                                             @RequestBody UpdateRoleRequest request) {
        roleService.updateRole(roleName, request);
        return ResponseEntity.ok("Role updated successfully");
    }

    @GetMapping
    public  ResponseEntity<Page<GetRoleResponse>> getRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }

}
