package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.OrderItemDto;
import com.smartshop.smartshop.entity.OrderItem;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.entity.Order;

public class OrderItemMapper {

    public static OrderItemDto toDto(OrderItem item) {
        if (item == null) return null;
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct() == null ? null : item.getProduct().getId());
        dto.setOrderId(item.getOrder() == null ? null : item.getOrder().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalLine(item.getTotalLine());
        return dto;
    }

    public static OrderItem toEntity(OrderItemDto dto) {
        if (dto == null) return null;
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (dto.getPrice() == null || dto.getPrice().signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
        OrderItem item = new OrderItem();
        item.setId(dto.getId());
        if (dto.getProductId() != null) {
            Product p = new Product();
            p.setId(dto.getProductId());
            item.setProduct(p);
        }
        if (dto.getOrderId() != null) {
            Order o = new Order();
            o.setId(dto.getOrderId());
            item.setOrder(o);
        }
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        item.setTotalLine(dto.getTotalLine());
        return item;
    }
}
