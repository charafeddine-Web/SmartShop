package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.OrderDto;
import com.smartshop.smartshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto created = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/orders/{id}/confirm")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable("id") Long id) {
        OrderDto confirmed = orderService.confirmOrder(id);
        return ResponseEntity.ok(confirmed);
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable("id") Long id) {
        OrderDto canceled = orderService.cancelOrder(id);
        return ResponseEntity.ok(canceled);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        OrderDto dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/clients/{clientId}/orders")
    public ResponseEntity<List<OrderDto>> getOrdersByClient(@PathVariable("clientId") Long clientId) {
        List<OrderDto> list = orderService.getOrdersByClientId(clientId);
        return ResponseEntity.ok(list);
    }
}
