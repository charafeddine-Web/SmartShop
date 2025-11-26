package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.OrderDto;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto dto);
}
