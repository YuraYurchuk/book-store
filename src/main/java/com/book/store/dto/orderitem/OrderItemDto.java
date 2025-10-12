package com.book.store.dto.orderitem;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
