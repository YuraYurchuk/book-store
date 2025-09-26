package com.book.store.service;

import com.book.store.dto.cartitem.CartItemsResponseDto;
import com.book.store.dto.cartitem.UpdateCartItemQuantityDto;

public interface CartItemService {

    CartItemsResponseDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId);

    void deleteById(Long id);
}
