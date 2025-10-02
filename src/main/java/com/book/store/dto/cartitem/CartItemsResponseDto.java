package com.book.store.dto.cartitem;

public record CartItemsResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {

}
