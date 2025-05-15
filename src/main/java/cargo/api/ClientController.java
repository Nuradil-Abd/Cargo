package cargo.api;

import cargo.dto.reponces.GetClientResponse;
import cargo.dto.requests.CreateClientRequest;
import cargo.exeptions.NotFoundException;
import cargo.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
//    @Secured("ADMIN")
public class ClientController {


    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody CreateClientRequest request) {
       clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Client successfully created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @RequestBody CreateClientRequest request) {
        clientService.updateClient(id, request);
        return ResponseEntity.ok("Client updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/deactivate/{clientId}")
    public ResponseEntity<String> deactivateClient(@PathVariable Long clientId) {
        try {
            clientService.deactivateClient(clientId);
            return ResponseEntity.ok("Client deactivated successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
