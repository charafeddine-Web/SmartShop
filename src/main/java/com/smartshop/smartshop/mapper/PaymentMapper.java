package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Payment;
import com.smartshop.smartshop.entity.Order;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        if (payment == null) return null;
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder() == null ? null : payment.getOrder().getId());
        dto.setPaymentNumber(payment.getPaymentNumber());
        dto.setAmountPaid(payment.getAmountPaid());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setDateEncaissement(payment.getDateEncaissement());
        dto.setStatus(payment.getStatus());
        return dto;
    }

    public static Payment toEntity(PaymentDto dto) {
        if (dto == null) return null;
        if (dto.getAmountPaid() == null || dto.getAmountPaid().signum() < 0) {
            throw new IllegalArgumentException("amountPaid must be non-negative");
        }
        Payment payment = new Payment();
        payment.setId(dto.getId());
        if (dto.getOrderId() != null) {
            Order o = new Order();
            o.setId(dto.getOrderId());
            payment.setOrder(o);
        }
        payment.setPaymentNumber(dto.getPaymentNumber());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setDateEncaissement(dto.getDateEncaissement());
        payment.setStatus(dto.getStatus());
        return payment;
    }
}
