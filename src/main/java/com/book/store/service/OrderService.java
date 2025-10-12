package com.book.store.service;

import com.book.store.dto.order.CreateOrderRequestDto;
import com.book.store.dto.order.OrderDto;
import com.book.store.dto.order.UpdateOrderStatusDto;
import com.book.store.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> findOrders(Pageable pageable);

    OrderDto saveOrder(CreateOrderRequestDto dto);

    OrderDto updateStatus(Long id, UpdateOrderStatusDto dto);

    List<OrderItemDto> findOrderItemsByOrderId(Long id);

    OrderItemDto findSpecificOrderItemById(Long orderId, Long itemId);
}
