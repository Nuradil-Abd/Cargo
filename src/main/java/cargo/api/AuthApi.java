package cargo.api;

import cargo.dto.reponces.SignResponse;
import cargo.dto.requests.SignInRequest;
import cargo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cargo.dto.requests.RegisterRequest;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApi {
    private final UserService userService;

    @PostMapping("/sign-up")
    @Operation(summary = "Register user")
    public ResponseEntity<?> signUp(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.signUp(registerRequest));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Login")
    public SignResponse signIn(@RequestBody SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }



}
