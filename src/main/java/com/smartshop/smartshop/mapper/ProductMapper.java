package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setDeleted(product.getDeleted());
        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (dto.getPrice() == null || dto.getPrice().signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity() == null ? 0 : dto.getStockQuantity());
        product.setDeleted(dto.getDeleted() == null ? false : dto.getDeleted());
        return product;
    }
}
