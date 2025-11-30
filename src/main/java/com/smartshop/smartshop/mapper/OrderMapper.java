package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.OrderDto;
import com.smartshop.smartshop.entity.Client;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PromoCodeMapper.class, OrderItemMapper.class, PaymentMapper.class })
public interface OrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "promoCode", target = "promoCode", qualifiedByName = "promoCodeToString")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payments", source = "payments",defaultExpression = "java(new java.util.ArrayList<>())")
    OrderDto toDto(Order order);

    @Mapping(target = "id", source = "id")
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "promoCode", target = "promoCode", qualifiedByName = "stringToPromoCode")
    Order toEntity(OrderDto dto);


    default Client map(Long clientId) {
        if (clientId == null) return null;
        Client c = new Client();
        c.setId(clientId);
        return c;
    }


}