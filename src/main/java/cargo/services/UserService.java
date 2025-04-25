package cargo.services;

import cargo.reponces.SignUpResponse;
import cargo.requests.RegisterRequest;

public interface UserService {
    SignUpResponse signUp(RegisterRequest registerRequest);
}
