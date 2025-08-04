package com.book.store.dto.user;

import com.book.store.annotation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank(message = "email is required")
    @Email
    private String email;
    @NotBlank(message = "password is required")
    @Length(min = 8, max = 20)
    private String password;
    @NotBlank(message = "repeatPassword is required")
    @Length(min = 8, max = 20)
    private String repeatPassword;
    @NotBlank(message = "firstName is required")
    private String firstName;
    @NotBlank(message = "lastName is required")
    private String lastName;
    private String shippingAddress;
}
