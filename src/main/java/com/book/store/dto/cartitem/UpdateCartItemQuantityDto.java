package com.book.store.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemQuantityDto {
    @Min(value = 1, message = "quantity is required")
    private int quantity;
}
