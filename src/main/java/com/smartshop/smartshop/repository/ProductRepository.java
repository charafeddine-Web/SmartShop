package com.smartshop.smartshop.repository;

import com.smartshop.smartshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndDeletedFalse(Long id);

}
