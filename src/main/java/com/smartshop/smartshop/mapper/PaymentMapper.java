package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.PaymentDto;
import com.smartshop.smartshop.entity.Payment;
import com.smartshop.smartshop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

     PaymentDto toDto(Payment payment);
     Payment toEntity(PaymentDto dto);

}
