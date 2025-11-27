package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ProductMapper;
import com.smartshop.smartshop.repository.ProductRepository;
import com.smartshop.smartshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto dto) {
        Product toSave = productMapper.toEntity(dto);
        // ensure deleted flag is false when creating
        if (toSave.getDeleted() == null) toSave.setDeleted(false);
        Product saved = productRepository.save(toSave);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product existingProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setStockQuantity(dto.getStockQuantity());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        existingProduct.setDeleted(true);
        productRepository.save(existingProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findByDeletedFalse().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

}