package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;

public class ClientMapper {

    public static ClientDto toDto(Client client) {
        if (client == null) return null;
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setUsername(client.getUsername());
        dto.setEmail(client.getEmail());
        dto.setFidelityLevel(client.getFidelityLevel());
        dto.setTotalOrders(client.getTotalOrders());
        dto.setTotalSpent(client.getTotalSpent());
        return dto;
    }

    public static Client toEntity(ClientDto dto) {
        if (dto == null) return null;
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("username is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("email is required");
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setFidelityLevel(dto.getFidelityLevel());
        client.setTotalOrders(dto.getTotalOrders() == null ? 0 : dto.getTotalOrders());
        client.setTotalSpent(dto.getTotalSpent() == null ? java.math.BigDecimal.ZERO : dto.getTotalSpent());
        return client;
    }
}
