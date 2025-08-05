package com.book.store.service;

import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
