package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.ProductMapper;
import com.smartshop.smartshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Prod");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("10.00"));
        product.setStockQuantity(5);
        product.setDeleted(false);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Prod");
        productDto.setDescription("Desc");
        productDto.setPrice(new BigDecimal("10.00"));
        productDto.setStockQuantity(5);
        productDto.setDeleted(false);
    }

    @Test
    void addProduct_successful() {
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto res = productService.addProduct(productDto);

        assertThat(res).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_successful() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto res = productService.getProductById(1L);
        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(1L);
    }

    @Test
    void getProductById_notFound_throws() {
        when(productRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(2L));
    }

    @Test
    void updateProduct_successful() {
        ProductDto toUpdate = new ProductDto();
        toUpdate.setName("New");
        toUpdate.setDescription("NewDesc");
        toUpdate.setPrice(new BigDecimal("20.00"));
        toUpdate.setStockQuantity(10);

        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto res = productService.updateProduct(1L, toUpdate);

        assertThat(res).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_notFound_throws() {
        when(productRepository.findByIdAndDeletedFalse(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(3L, productDto));
    }

    @Test
    void deleteProduct_successful() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deleteProduct(1L);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_notFound_throws() {
        when(productRepository.findByIdAndDeletedFalse(4L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(4L));
    }

    @Test
    void getAllProducts_successful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findByDeletedFalse(pageable)).thenReturn(page);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        var res = productService.getAllProducts(pageable);
        assertThat(res).isNotNull();
        assertThat(res.getTotalElements()).isEqualTo(1);
    }

}

