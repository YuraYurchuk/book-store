package com.book.store.dto.cartItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemsRequestDto {
    @NotNull(message = "book id is required")
    private Long bookId;
    @Min(value = 1, message = "quantity is required")
    private int quantity;
}
