package com.book.store.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank(message = "name is required")
    private String name;
    private String description;
}
