package com.book.store.service.impl;

import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.mapper.UserMapper;
import com.book.store.model.Role;
import com.book.store.model.User;
import com.book.store.repository.role.RoleRepository;
import com.book.store.repository.user.UserRepository;
import com.book.store.service.ShoppingCartService;
import com.book.store.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user, because user with email "
                    + requestDto.getEmail() + " is already exist");
        }
        User user = userMapper.toModel(requestDto);
        Role role = roleRepository.findByRole(Role.RoleName.ROLE_USER).orElseThrow(()
                -> new RegistrationException("Can't find role " + Role.RoleName.ROLE_USER));
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(user);
        shoppingCartService.eddShoppingCartForNewUser(savedUser);
        return userMapper.toDto(savedUser);
    }
}
