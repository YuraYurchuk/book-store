package com.book.store.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank(message = "shipping address is required")
    private String shippingAddress;
}
