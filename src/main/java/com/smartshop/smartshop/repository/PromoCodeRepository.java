package com.smartshop.smartshop.repository;

import com.smartshop.smartshop.dto.PromoCodeDto;
import com.smartshop.smartshop.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {
    Boolean existsByCode(String code);
    Optional<PromoCode> findByCodeAndAvailabilityStatusFalse(String code);

}
