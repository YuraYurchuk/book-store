package com.book.store.dto.order;

import com.book.store.dto.orderitem.OrderItemDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItemDto,
        LocalDateTime orderDate,
        BigDecimal total,
        String status
) {
}
