package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ProductMapper;
import com.smartshop.smartshop.repository.ProductRepository;
import com.smartshop.smartshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto addProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        if (product.getDeleted() == null) {
            product.setDeleted(false);
        }
        Product saved = productRepository.save(product);
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
        public Page<ProductDto> getAllProducts(Pageable pageable) {
            Page<Product> page = productRepository.findByDeletedFalse(pageable);

            return page.map(productMapper::toDto);
        }


}