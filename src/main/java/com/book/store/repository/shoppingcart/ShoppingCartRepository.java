package com.book.store.repository.shoppingcart;

import com.book.store.model.ShoppingCart;
import com.book.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findShoppingCartByUser(User user);
}
