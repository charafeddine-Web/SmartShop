package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ClientDto;
import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ProductMapper;
import com.smartshop.smartshop.repository.ProductRepository;
import com.smartshop.smartshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDto addProduct(ProductDto dto) {
        Product saved = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toDto(saved);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        return productMapper.toDto(product);
    }

    ;

    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setStockQuantity(dto.getStockQuantity());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    ;

    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        existingProduct.setDeleted(true);
        productRepository.save(existingProduct);
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDto> products = productRepository.findAll().stream()
                .filter(p-> p.getDeleted().equals(false))
                .map(productMapper::toDto)
                .toList();
        return products;
    }

}