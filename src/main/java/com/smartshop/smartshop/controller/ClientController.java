package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.dto.spec.Creation;
import com.smartshop.smartshop.dto.spec.Update;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<?> addClient(@Validated(Creation.class) @RequestBody ClientDto dto) {
        ClientDto client = clientService.addClient(dto);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Validated(Update.class) @RequestBody ClientDto dto) {
        ClientDto updatedClient = clientService.updateClient(id, dto);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

}
