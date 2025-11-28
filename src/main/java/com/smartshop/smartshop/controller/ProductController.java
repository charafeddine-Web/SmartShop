package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private  final ProductService productService;

    @PostMapping("/save")
    public ProductDto addProduct(@Valid @RequestBody ProductDto dto){
        return productService.addProduct(dto);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto dto){
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productService.getAllProducts( pageable);
    }

}
