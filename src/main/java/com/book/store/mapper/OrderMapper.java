package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.order.OrderDto;
import com.book.store.model.Order;
import com.book.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippingAddress",ignore = true)
    Order fromShoppingCart(ShoppingCart shoppingCart);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemDto", source = "orderItems", qualifiedByName = "toOrderItemDto")
    OrderDto toDto(Order order);
}
