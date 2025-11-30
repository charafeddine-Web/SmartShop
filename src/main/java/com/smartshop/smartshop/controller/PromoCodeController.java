package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.PromoCodeDto;
import com.smartshop.smartshop.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promocodes")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping("/generate")
    public ResponseEntity<PromoCodeDto> generatePromoCode() {
        PromoCodeDto dto = promoCodeService.generateUniqueCode();
        return ResponseEntity.ok(dto);
    }


}
