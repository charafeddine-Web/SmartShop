package com.smartshop.smartshop.service;

import com.smartshop.smartshop.dto.ProductDto;

import java.util.List;

public interface ProductService {

    public ProductDto addProduct(ProductDto dto);
    public ProductDto getProductById(Long id);
    public ProductDto updateProduct(Long id, ProductDto dto);
    public void deleteProduct(Long id);
    public List<ProductDto> getAllProducts();
}

