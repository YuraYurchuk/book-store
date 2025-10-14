package com.book.store.controller;

import com.book.store.dto.order.CreateOrderRequestDto;
import com.book.store.dto.order.OrderDto;
import com.book.store.dto.order.UpdateOrderStatusDto;
import com.book.store.dto.orderitem.OrderItemDto;
import com.book.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Order",
        description = "Operations for managing the user's orders and order items"
)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Get user's order history (paginated)",
            description = "Retrieves a paginated list of orders for the authenticated user."
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<OrderDto> getOrdersHistory(Pageable pageable) {
        return orderService.findOrders(pageable);
    }

    @Operation(
            summary = "Create and save a new order",
            description = "Creates a new order based on the authenticated user's shopping cart. "
                    + "The request body must contain the shipping address. "
                    + "All items from the user's cart will be added to this order, "
                    + "and the cart will be cleared."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public OrderDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto dto
    ) {
        return orderService.saveOrder(dto);
    }

    @Operation(
            summary = "Change order status",
            description = "Updates the status of a specific order. "
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto chengStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusDto status
    ) {
        return orderService.updateStatus(id, status);
    }

    @Operation(
            summary = "Get all order items for a specific order",
            description = "Returns a list of all items that belong to the specified order ID. "
    )
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.findOrderItemsByOrderId(orderId);
    }

    @Operation(
            summary = "Get a specific order item by ID",
            description = "Retrieves a single order item by its ID within the specified order. "
    )
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getSpecificOrderItemById(
            @PathVariable Long orderId, @PathVariable Long itemId
    ) {
        return orderService.findSpecificOrderItemById(orderId, itemId);
    }
}
