package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.service.ClientService;
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
    public ResponseEntity<?> addClient(@RequestBody ClientDto dto) {
        Client client = clientService.addClient(dto);
        return ResponseEntity.ok(client);
    }
}

