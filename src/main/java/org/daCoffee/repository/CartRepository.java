package org.daCoffee.repository;

import org.daCoffee.entity.Cart;
import org.daCoffee.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, CartId> {
}