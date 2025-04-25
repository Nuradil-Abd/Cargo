package cargo.api;

import cargo.reponces.SignUpResponse;
import cargo.requests.RegisterRequest;
import cargo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
@Secured("ADMIN")
public class AdminController {


    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }
}
