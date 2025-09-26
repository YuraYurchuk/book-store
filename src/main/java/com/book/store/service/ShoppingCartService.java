package com.book.store.service;

import com.book.store.dto.cartitem.CartItemsResponseDto;
import com.book.store.dto.cartitem.CreateCartItemsRequestDto;
import com.book.store.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemsResponseDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto);
}
