package com.book.store.service.impl;

import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.mapper.UserMapper;
import com.book.store.model.User;
import com.book.store.repository.user.UserRepository;
import com.book.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (repository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user, because user with email "
                    + requestDto.getEmail() + " is already exist");
        }
        User user = mapper.toModel(requestDto);
        return mapper.toDto(repository.save(user));
    }
}
