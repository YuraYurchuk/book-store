package com.book.store.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Author is required")
    private String author;
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "\\d{13}", message = "ISBN mast be 13 digits")
    private String isbn;
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price mast be grater then 0")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
