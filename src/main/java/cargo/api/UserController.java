package cargo.api;

import cargo.dto.reponces.GetRoleResponse;
import cargo.dto.reponces.GetUserResponse;
import cargo.dto.requests.UpdateUserRequest;
import cargo.entity.User;
import cargo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
@Secured("ADMIN")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<GetUserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<GetUserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
