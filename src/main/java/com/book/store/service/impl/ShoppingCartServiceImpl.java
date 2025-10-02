package com.book.store.service.impl;

import com.book.store.dto.cartitem.CreateCartItemsRequestDto;
import com.book.store.dto.cartitem.UpdateCartItemQuantityDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
    public ShoppingCartDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id " + requestDto.getBookId())
        );
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItemsRepository.save(cartItem);
        return getShoppingCart();
    }

    @Override
    public ShoppingCartDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId) {
        CartItem cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id " + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemsRepository.save(cartItem);
        return getShoppingCart();
    }

    @Override
    public void deleteById(Long id) {
        cartItemsRepository.deleteById(id);
    }

    @Override
    public void eddShoppingCartForNewUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User userFromDb = userRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id " + user.getId())
        );
        return shoppingCartRepository.findShoppingCartByUser(userFromDb);
    }
}
