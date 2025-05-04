package cargo.services;

import cargo.dto.reponces.GetUserResponse;
import cargo.dto.reponces.SignResponse;
import cargo.dto.requests.RegisterRequest;
import cargo.dto.requests.RegisterWithRole;
import cargo.dto.requests.SignInRequest;
import cargo.dto.requests.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    SignResponse signUp(RegisterRequest registerRequest);

    SignResponse signIn(SignInRequest signInRequest);

    SignResponse createUserByAdmin(RegisterWithRole request);

    Page<GetUserResponse> getAllUsers(Pageable pageable);

    GetUserResponse updateUser(Long userId, UpdateUserRequest request);

    void deleteUser(Long userId);
}
