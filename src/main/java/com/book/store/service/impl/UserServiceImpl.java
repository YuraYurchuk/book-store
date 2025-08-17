package com.book.store.service.impl;

import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.mapper.UserMapper;
import com.book.store.model.Role;
import com.book.store.model.User;
import com.book.store.repository.role.RoleRepository;
import com.book.store.repository.user.UserRepository;
import com.book.store.service.UserService;
import java.security.SecureRandom;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder
            = new BCryptPasswordEncoder(10, new SecureRandom());

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user, because user with email "
                    + requestDto.getEmail() + " is already exist");
        }
        User user = userMapper.toModel(requestDto);
        Role role = roleRepository.findByRole(Role.RoleName.USER).orElseThrow(()
                -> new RegistrationException("Can't find role " + Role.RoleName.USER));
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}
