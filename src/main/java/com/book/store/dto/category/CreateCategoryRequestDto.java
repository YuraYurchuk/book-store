package com.book.store.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotBlank(message = "name is required")
    private String name;
    private String description;
}
