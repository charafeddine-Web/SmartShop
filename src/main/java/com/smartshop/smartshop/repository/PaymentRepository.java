package com.smartshop.smartshop.repository;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment>  findByOrder_IdOrderByPaymentNumber(Long orderId);
}

