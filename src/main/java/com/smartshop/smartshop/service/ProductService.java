package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {

     ProductDto addProduct(ProductDto dto);
     ProductDto getProductById(Long id);
     ProductDto updateProduct(Long id, ProductDto dto);
     void deleteProduct(Long id);
     Page<ProductDto> getAllProducts(Pageable pageable);
}

