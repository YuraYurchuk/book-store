package com.book.store.service;

import com.book.store.dto.cartItem.CartItemsResponseDto;
import com.book.store.dto.cartItem.CreateCartItemsRequestDto;
import com.book.store.dto.cartItem.UpdateCartItemQuantityDto;

public interface CartItemService {

    CartItemsResponseDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId);
    void deleteById(Long id);
}
