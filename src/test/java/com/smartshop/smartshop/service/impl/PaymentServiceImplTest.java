package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Order;
import com.smartshop.smartshop.entity.Payment;
import com.smartshop.smartshop.entity.enums.PaymentMethod;
import com.smartshop.smartshop.entity.enums.PaymentStatus;
import com.smartshop.smartshop.exception.BusinessRuleException;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.PaymentMapper;
import com.smartshop.smartshop.repository.OrderRepository;
import com.smartshop.smartshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setStatus(com.smartshop.smartshop.entity.enums.OrderStatus.PENDING);
        order.setRemainingAmount(new BigDecimal("100.00"));
        order.setPayments(new ArrayList<>());
    }

    @Test
    void addPayment_success_cash_under_limit() {
        PaymentDto dto = new PaymentDto();
        dto.setAmountPaid(new BigDecimal("50.00"));
        dto.setPaymentMethod(PaymentMethod.CASH);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Payment saved = new Payment();
        saved.setId(10L);
        saved.setAmountPaid(dto.getAmountPaid());
        saved.setStatus(PaymentStatus.ENCAISSÉ);
        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);
        when(paymentMapper.toDto(saved)).thenReturn(dto);

        PaymentDto res = paymentService.addPayment(1L, dto);

        assertThat(res).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void addPayment_orderNotFound_throws() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> paymentService.addPayment(2L, new PaymentDto()));
    }

    @Test
    void addPayment_cash_over_limit_throws() {
        PaymentDto dto = new PaymentDto();
        dto.setAmountPaid(new BigDecimal("25000"));
        dto.setPaymentMethod(PaymentMethod.CASH);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(BusinessRuleException.class, () -> paymentService.addPayment(1L, dto));
    }

    @Test
    void validatePayment_alreadyEncaisse_throws() {
        Payment payment = new Payment();
        payment.setId(5L);
        payment.setStatus(PaymentStatus.ENCAISSÉ);
        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
        assertThrows(BusinessRuleException.class, () -> paymentService.validatePayment(5L));
    }

    @Test
    void validatePayment_success() {
        Payment payment = new Payment();
        payment.setId(6L);
        payment.setStatus(PaymentStatus.EN_ATTENTE);
        when(paymentRepository.findById(6L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(new PaymentDto());

        var res = paymentService.validatePayment(6L);
        assertThat(res).isNotNull();
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void rejectPayment_success() {
        Payment payment = new Payment();
        payment.setId(7L);
        payment.setStatus(PaymentStatus.EN_ATTENTE);
        payment.setAmountPaid(new BigDecimal("20.00"));
        payment.setOrder(order);

        when(paymentRepository.findById(7L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(new PaymentDto());

        var res = paymentService.rejectPayment(7L);
        assertThat(res).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

}

