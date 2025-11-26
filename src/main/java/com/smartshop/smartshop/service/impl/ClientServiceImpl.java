package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.entity.User;
import com.smartshop.smartshop.entity.enums.CustomerTier;
import com.smartshop.smartshop.entity.enums.UserRole;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ClientMapper;
import com.smartshop.smartshop.repository.ClientRepository;
import com.smartshop.smartshop.repository.UserRepository;
import com.smartshop.smartshop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDto addClient(ClientDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(UserRole.CLIENT);
        User savedUser = userRepository.save(user);

        Client client = new Client();
        client.setUser(savedUser);
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel() != null ? dto.getFidelityLevel() : CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        Client savedClient = clientRepository.save(client);

        return clientMapper.toDto(savedClient);
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

    public ClientDto updateClient(Long id, ClientDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        User user = client.getUser();
        if(user != null) {
            user.setUsername(dto.getUsername());
            userRepository.save(user);
        }
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel());
        client.setTotalOrders(dto.getTotalOrders());
        client.setTotalSpent(dto.getTotalSpent());

        Client updated = clientRepository.save(client);

        return clientMapper.toDto(updated);

    }


    public void deleteClient(Long id){
        clientRepository.deleteById(id);
    };

}
