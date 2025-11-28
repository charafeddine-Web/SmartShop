package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.PromoCodeDto;
import com.smartshop.smartshop.entity.PromoCode;
import com.smartshop.smartshop.mapper.PromoCodeMapper;
import com.smartshop.smartshop.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl {

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    private static final String CHARACTERS = "ZPOIUHJRMJDBVHBSJBSKDJEWQ1234567890";

    public PromoCodeDto generateUniqueCode() {
        String code;
        do {
            code = "PROMO-" + generateRandomCode();
        } while (promoCodeRepository.existsByCode(code));

        PromoCode promo = new PromoCode();
        promo.setCode(code);
        promo.setAvailabilityStatus(false);
        return promoCodeMapper.toDto(promoCodeRepository.save(promo));
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
