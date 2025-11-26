package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<?> addClient(@Valid @RequestBody ClientDto dto) {
        ClientDto client = clientService.addClient(dto);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getClientById(@RequestBody Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }



}
