package com.book.store.dto.shoppingcart;

import com.book.store.dto.cartitem.CartItemsResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemsResponseDto> cartItems;
}
