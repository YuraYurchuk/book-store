package com.book.store.dto.shoppingCart;

import com.book.store.dto.cartItem.CartItemsResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemsResponseDto> cartItems;
}
