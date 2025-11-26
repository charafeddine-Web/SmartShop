package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.entity.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

     ClientDto toDto(Client client);
     Client toEntity(ClientDto dto);
    List<ClientDto> toDto(List<Client> clients);

    List<Client> toEntity(List<ClientDto> dtos);
}
