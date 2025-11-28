package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.PromoCodeDto;
import com.smartshop.smartshop.entity.PromoCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    PromoCode toEntity(PromoCodeDto promoCodedto);
    PromoCodeDto toDto(PromoCode promoCode);
}
