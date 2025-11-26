package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.ProductDto;
import com.smartshop.smartshop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

     ProductDto toDto(Product product) ;
     Product toEntity(ProductDto dto);
}
