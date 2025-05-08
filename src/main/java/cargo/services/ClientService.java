package cargo.services;

import cargo.dto.reponces.GetClientResponse;
import cargo.dto.requests.CreateClientRequest;

public interface ClientService {
    void createClient(CreateClientRequest request);

    void updateClient(Long id, CreateClientRequest request);

    void deleteClient(Long id);

    GetClientResponse getClientById(Long id);

    void deactivateClient(Long clientId);
}
