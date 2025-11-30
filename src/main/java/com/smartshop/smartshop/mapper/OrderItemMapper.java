package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.OrderItemDto;
import com.smartshop.smartshop.entity.OrderItem;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

     @Mapping(source = "product.id", target = "productId")
     @Mapping(source = "order.id", target = "orderId")
     OrderItemDto toDto(OrderItem item);

     @Mapping(target = "product", ignore = true)
     @Mapping(target = "order", ignore = true)
     OrderItem toEntity(OrderItemDto dto);

}