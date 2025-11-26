package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;

import java.util.List;

public interface ClientService {

    public ClientDto addClient(ClientDto dto);

    public ClientDto getClientById(Long id);

    public List<ClientDto> getAllClients();

    public ClientDto updateClient(Long id, ClientDto dto);

    public void deleteClient(Long id);

}

