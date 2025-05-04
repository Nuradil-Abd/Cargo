package cargo.api;

import cargo.dto.reponces.SignResponse;
import cargo.dto.requests.RegisterWithRole;
import cargo.services.UserService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/create-user")
    public SignResponse createUserByAdmin(@RequestBody RegisterWithRole request) {
        return userService.createUserByAdmin(request);
    }



}
