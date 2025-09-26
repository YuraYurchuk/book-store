package com.book.store.service.impl;

import com.book.store.dto.cartitem.CartItemsResponseDto;
import com.book.store.dto.cartitem.UpdateCartItemQuantityDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.CartItemMapper;
import com.book.store.model.CartItem;
import com.book.store.repository.cartitem.CartItemsRepository;
import com.book.store.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemsRepository cartItemsRepository;

    @Override
    public CartItemsResponseDto updateQuantity(
            UpdateCartItemQuantityDto requestDto,
            Long cartItemId) {
        CartItem cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id " + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemsRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemsRepository.deleteById(id);
    }
}
