package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ClientMapper;
import com.smartshop.smartshop.repository.ClientRepository;
import com.smartshop.smartshop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;


    @Override
    public ClientDto addClient(ClientDto dto){
        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel());
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        Client saved = clientRepository.save(client);
        return clientMapper.toDto(saved);
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return clientMapper.toDto(client);
    }

    public List<ClientDto> getAllClients(){
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDto(clients);
    };

    public ClientDto updateClient(Long id, ClientDto dto){
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel());
        client.setTotalOrders(dto.getTotalOrders());
        client.setTotalSpent(dto.getTotalSpent());
        Client updated = clientRepository.save(client);
        return clientMapper.toDto(updated);
    };

    public void deleteClient(Long id){
        clientRepository.deleteById(id);
    };

}
