package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;

public interface ClientService {

    public ClientDto addClient(ClientDto dto);

    public ClientDto getClientById(Long id);

}

