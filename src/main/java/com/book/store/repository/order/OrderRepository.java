package com.book.store.repository.order;

import com.book.store.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser_Id(Long userId, Pageable pageable);
}
