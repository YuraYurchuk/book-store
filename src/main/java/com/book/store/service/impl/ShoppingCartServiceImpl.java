package com.book.store.service.impl;

import com.book.store.dto.cartitem.CartItemsResponseDto;
import com.book.store.dto.cartitem.CreateCartItemsRequestDto;
import com.book.store.dto.shoppingcart.ShoppingCartDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.CartItemMapper;
import com.book.store.mapper.ShoppingCartMapper;
import com.book.store.model.Book;
import com.book.store.model.CartItem;
import com.book.store.model.ShoppingCart;
import com.book.store.model.User;
import com.book.store.repository.book.BookRepository;
import com.book.store.repository.cartitem.CartItemsRepository;
import com.book.store.repository.shoppingcart.ShoppingCartRepository;
import com.book.store.repository.user.UserRepository;
import com.book.store.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemsRepository cartItemsRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemsResponseDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id " + requestDto.getBookId())
        );
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemsRepository.save(cartItem));
    }

    private ShoppingCart getShoppingCartByUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by email " + userName)
        );
        return shoppingCartRepository.findShoppingCartByUser(user);
    }
}
