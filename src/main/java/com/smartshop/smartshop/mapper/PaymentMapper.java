package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Payment;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

     @Mapping(source = "order.id", target = "orderId")
     @Mapping(source = "paymentNumber", target = "paymentNumber")
     @Mapping(source = "dateEncaissement", target = "dateEncaissement")
     PaymentDto toDto(Payment payment);
     Payment toEntity(PaymentDto dto);

}