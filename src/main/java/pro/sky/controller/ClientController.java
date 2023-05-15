package pro.sky.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.entity.Client;
import pro.sky.services.ClientService;

import java.util.Optional;

@RestController
@RequestMapping("/client")
@Tag(name = "Клиенты", description = "Операции создания записей о новых клиентах и получения данных о них")
public class ClientController {
private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        if (client.getName() == null || client.getPhoneNumber() == null ||
                client.getAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clientService.saveClientToRepository(client));
    }


    @GetMapping("/get")
    public ResponseEntity<Optional<Client>> getClient(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Client> client = clientService.findClient(id);
        return ResponseEntity.ok(client);
    }

}
