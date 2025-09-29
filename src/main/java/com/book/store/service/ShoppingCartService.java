package com.book.store.service;

import com.book.store.dto.cartitem.CreateCartItemsRequestDto;
import com.book.store.dto.cartitem.UpdateCartItemQuantityDto;
import com.book.store.dto.shoppingcart.ShoppingCartDto;
import com.book.store.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    ShoppingCartDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto);

    ShoppingCartDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId);

    void deleteById(Long id);

    void eddShoppingCartForNewUser(User user);
}
