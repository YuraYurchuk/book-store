package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.shoppingCart.ShoppingCartDto;
import com.book.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems",
            qualifiedByName = "getCartItemsResponseDto")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
