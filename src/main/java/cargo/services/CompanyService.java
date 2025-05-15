package cargo.services;

import cargo.dto.requests.CreateCompanyRequest;
import cargo.dto.requests.CreateUserRequest;


public interface CompanyService {
    void create(CreateCompanyRequest request);


    void createUserWithCompany(CreateUserRequest request);
}

