package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.PromoCodeDto;
import com.smartshop.smartshop.entity.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    PromoCode toEntity(PromoCodeDto promoCodedto);
    PromoCodeDto toDto(PromoCode promoCode);

    @Named("stringToPromoCode")
    default PromoCode stringToPromoCode(String code) {
        if (code == null) return null;
        PromoCode promo = new PromoCode();
        promo.setCode(code);
        return promo;
    }
    @Named("promoCodeToString")
    default String promoCodeToString(PromoCode promoCode) {
        return promoCode != null ? promoCode.getCode() : null;
    }

}
