package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.repository.ClientRepository;
import com.smartshop.smartshop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public Client addClient(ClientDto dto){
        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel());
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        return clientRepository.save(client);
    }
}

