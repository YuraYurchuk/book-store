package com.book.store.service.impl;

import com.book.store.dto.order.CreateOrderRequestDto;
import com.book.store.dto.order.OrderDto;
import com.book.store.dto.order.UpdateOrderStatusDto;
import com.book.store.dto.orderitem.OrderItemDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.OrderItemMapper;
import com.book.store.mapper.OrderMapper;
import com.book.store.model.CartItem;
import com.book.store.model.Order;
import com.book.store.model.OrderItem;
import com.book.store.model.ShoppingCart;
import com.book.store.model.User;
import com.book.store.repository.order.OrderRepository;
import com.book.store.repository.shoppingcart.ShoppingCartRepository;
import com.book.store.repository.user.UserRepository;
import com.book.store.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;

    @Override
    public Page<OrderDto> findOrders(Pageable pageable) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        return orderRepository.findAllByUser_Id(shoppingCart.getUser().getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto saveOrder(CreateOrderRequestDto dto) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping Cart is empty");
        }
        Order order = orderMapper.fromShoppingCart(shoppingCart);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(getTotalPrice(shoppingCart.getCartItems()));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());
        order.setOrderItems(getOrderItemFroCartItem(shoppingCart.getCartItems(), order));
        Order orderFromDb = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(orderFromDb);
    }

    @Override
    public OrderDto updateStatus(Long id, UpdateOrderStatusDto dto) {
        Order order = findOrderById(id);
        order.setStatus(dto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(Long id) {
        Order order = findOrderById(id);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findSpecificOrderItemById(Long orderId, Long itemId) {
        Order order = findOrderById(orderId);
        OrderItem orderItem = order.getOrderItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find orderItem by id " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    private ShoppingCart getShoppingCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User userFromDb = userRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id " + user.getId())
        );
        return shoppingCartRepository.findShoppingCartByUser(userFromDb);
    }

    private BigDecimal getTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(c -> c.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<OrderItem> getOrderItemFroCartItem(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(orderItemMapper::toOrderItemFromCartItem)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toSet());
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id " + id));
    }

}
