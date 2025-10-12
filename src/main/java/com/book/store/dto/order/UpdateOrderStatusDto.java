package com.book.store.dto.order;

import com.book.store.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    @NotNull(message = "status is required")
    private Order.Status status;
}
