package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.cartItem.CartItemsResponseDto;
import com.book.store.dto.cartItem.CreateCartItemsRequestDto;
import com.book.store.model.CartItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    CartItem toModel(CreateCartItemsRequestDto requestDto);
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemsResponseDto toDto(CartItem cartItem);

    @Named("getCartItemsResponseDto")
    default Set<CartItemsResponseDto> getResponseDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
