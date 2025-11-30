package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.PromoCodeDto;

public interface PromoCodeService {

    public PromoCodeDto generateUniqueCode();

    public PromoCodeDto getByCode(String code);

}
