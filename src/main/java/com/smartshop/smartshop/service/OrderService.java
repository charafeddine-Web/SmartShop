package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto confirmOrder(Long orderId);

    OrderDto cancelOrder(Long orderId);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getOrdersByClientId(Long clientId);
}
