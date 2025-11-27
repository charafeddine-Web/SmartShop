package com.smartshop.smartshop.repository;

import com.smartshop.smartshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByDeletedFalse();

    Optional<Product> findByIdAndDeletedFalse(Long id);

}
