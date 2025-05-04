package cargo.services;

import cargo.dto.reponces.GetRoleResponse;
import cargo.dto.requests.RoleDeactivationRequest;
import cargo.dto.requests.RoleRequest;
import cargo.dto.requests.UpdateRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoleService {
   Page<GetRoleResponse> getAllRoles(Pageable pageable);

   void createRole(RoleRequest request);

   void updateRole(String roleId, UpdateRoleRequest request);

   void deactivateRoleWithAdminPassword(RoleDeactivationRequest request);

   void deleteRole(String roleName, String adminPassword);
}

