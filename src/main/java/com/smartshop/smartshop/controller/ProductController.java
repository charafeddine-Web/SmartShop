package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.dto.spec.Creation;
import com.smartshop.smartshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private  final ProductService productService;

    @PostMapping("/save")
    public ProductDto addProduct(@Validated(Creation.class) @RequestBody ProductDto dto){
        return productService.addProduct(dto);
    };

    @PostMapping("/get")
    public ProductDto getProductById(Long id){
        return productService.getProductById(id);
    };

    @PutMapping("/update")
    public ProductDto updateProduct(Long id, ProductDto dto){
        return productService.updateProduct(id, dto);
    };

    @PostMapping("/delete")
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    };

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }


}
