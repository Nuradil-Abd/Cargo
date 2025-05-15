package cargo.api;

import cargo.dto.requests.CreateCompanyRequest;
import cargo.dto.requests.CreateUserRequest;
import cargo.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateCompanyRequest request) {
        companyService.create(request);
        return ResponseEntity.ok("Company successfully created");
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUserAndAssignCompany(@RequestBody CreateUserRequest request) {
        companyService.createUserWithCompany(request);
        return ResponseEntity.ok("User successfully created and assigned company");
    }



}
