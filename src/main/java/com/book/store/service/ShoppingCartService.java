package com.book.store.service;

import com.book.store.dto.cartItem.CartItemsResponseDto;
import com.book.store.dto.cartItem.CreateCartItemsRequestDto;
import com.book.store.dto.shoppingCart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemsResponseDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto);
}
