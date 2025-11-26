package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.OrderItemDto;
import com.smartshop.smartshop.entity.OrderItem;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

     OrderItemDto toDto(OrderItem item);
     OrderItem toEntity(OrderItemDto dto);

}
