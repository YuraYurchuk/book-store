package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.orderitem.OrderItemDto;
import com.book.store.model.CartItem;
import com.book.store.model.OrderItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price",source = "book.price")
    OrderItem toOrderItemFromCartItem(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Named("toOrderItemDto")
    default Set<OrderItemDto> toOrderItemDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
