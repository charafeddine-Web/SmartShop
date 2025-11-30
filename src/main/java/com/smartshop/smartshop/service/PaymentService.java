package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto addPayment(Long orderId, PaymentDto paymentDto);
    PaymentDto validatePayment(Long paymentId);
    PaymentDto rejectPayment(Long paymentId);
    List<PaymentDto> getPaymentsByOrderId(Long orderId);
}