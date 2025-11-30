package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Order;
import com.smartshop.smartshop.entity.Payment;
import com.smartshop.smartshop.entity.enums.OrderStatus;
import com.smartshop.smartshop.entity.enums.PaymentMethod;
import com.smartshop.smartshop.entity.enums.PaymentStatus;
import com.smartshop.smartshop.exception.BusinessRuleException;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.PaymentMapper;
import com.smartshop.smartshop.repository.OrderRepository;
import com.smartshop.smartshop.repository.PaymentRepository;
import com.smartshop.smartshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentDto addPayment(Long orderId, PaymentDto paymentDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Impossible d'ajouter un paiement à une commande qui n'est plus en attente");
        }

        BigDecimal amountPaid = paymentDto.getAmountPaid();

        if (paymentDto.getPaymentMethod() == PaymentMethod.CASH && amountPaid.compareTo(new BigDecimal("20000")) > 0) {
            throw new BusinessRuleException("Le paiement en espèces ne peut pas dépasser 20 000 DH");
        }

        if (amountPaid.compareTo(order.getRemainingAmount()) > 0) {
            throw new BusinessRuleException("Le montant du paiement dépasse le montant restant à payer sur la commande");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentNumber((long) (order.getPayments() != null ? order.getPayments().size() + 1 : 1));
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setPaymentDate(LocalDateTime.now());

        if (PaymentMethod.CASH == paymentDto.getPaymentMethod()) {
            payment.setStatus(PaymentStatus.ENCAISSÉ);
            payment.setDateEncaissement(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.EN_ATTENTE);
            payment.setDateEncaissement(null);
        }

        Payment savedPayment = paymentRepository.save(payment);

        order.setRemainingAmount(order.getRemainingAmount().subtract(amountPaid));
        orderRepository.save(order);

        return paymentMapper.toDto(savedPayment);
    }

    public PaymentDto validatePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.ENCAISSÉ) {
            throw new BusinessRuleException("Ce paiement est déjà encaissé");
        }

        if (payment.getStatus() == PaymentStatus.REJETÉ) {
            throw new BusinessRuleException("Ce paiement a déjà été rejeté");
        }

        payment.setStatus(PaymentStatus.ENCAISSÉ);
        payment.setDateEncaissement(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    public PaymentDto rejectPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.ENCAISSÉ) {
            throw new BusinessRuleException("Impossible de rejeter un paiement déjà encaissé");
        }

        payment.setStatus(PaymentStatus.REJETÉ);
        Payment savedPayment = paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setRemainingAmount(order.getRemainingAmount().add(payment.getAmountPaid()));
        orderRepository.save(order);

        return paymentMapper.toDto(savedPayment);
    }

    public List<PaymentDto> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrder_IdOrderByPaymentNumber(orderId)
                .stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }
}