package com.book.store.dto.cartItem;

public record CartItemsResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {

}
