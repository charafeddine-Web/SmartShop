package com.smartshop.smartshop.mapper;

import com.smartshop.smartshop.dto.UserDto;
import com.smartshop.smartshop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
