package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
