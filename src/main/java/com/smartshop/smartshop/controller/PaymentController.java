package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<PaymentDto> addPayment(
            @PathVariable Long orderId,
            @RequestBody PaymentDto paymentDto) {

        PaymentDto created = paymentService.addPayment(orderId, paymentDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{paymentId}/validate")
    public ResponseEntity<PaymentDto> validatePayment(@PathVariable Long paymentId) {
        PaymentDto validated = paymentService.validatePayment(paymentId);
        return ResponseEntity.ok(validated);
    }

    @PutMapping("/{paymentId}/reject")
    public ResponseEntity<PaymentDto> rejectPayment(@PathVariable Long paymentId) {
        PaymentDto rejected = paymentService.rejectPayment(paymentId);
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<PaymentDto> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
}