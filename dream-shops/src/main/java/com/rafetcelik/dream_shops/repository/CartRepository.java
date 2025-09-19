package com.rafetcelik.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.dream_shops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Cart findByUserId(Long userId);

}
