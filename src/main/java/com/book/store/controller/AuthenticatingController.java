package com.book.store.controller;

import com.book.store.dto.user.UserLoginRequestDto;
import com.book.store.dto.user.UserLoginResponseDto;
import com.book.store.dto.user.UserRegistrationRequestDto;
import com.book.store.dto.user.UserResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.security.AuthenticationService;
import com.book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "Operation related to register and login users")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticatingController {
    private final UserService service;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "register new users",
            description = "Creates and saves a new user in the database"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return service.register(requestDto);
    }

    @Operation(
            summary = "Login for registered users",
            description = "Authenticates a user by verifying email and password. "
                   + "Returns a JWT token if credentials are valid."
    )
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
