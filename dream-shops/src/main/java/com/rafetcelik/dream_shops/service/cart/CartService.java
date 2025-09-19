package com.rafetcelik.dream_shops.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafetcelik.dream_shops.exception.ResourceNotFoundException;
import com.rafetcelik.dream_shops.model.Cart;
import com.rafetcelik.dream_shops.model.User;
import com.rafetcelik.dream_shops.repository.CartItemRepository;
import com.rafetcelik.dream_shops.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
	
	private final CartRepository cartRepository;
	
	private final CartItemRepository cartItemRepository;
	
	@Override
	public Cart getCart(Long id) {
		Cart cart = cartRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sepet bulunamadı!"));
		BigDecimal totalAmount = cart.getTotalAmount();
		cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}
	
	@Transactional
	@Override
	public void clearCart(Long id) {
		Cart cart = getCart(id);
		cartItemRepository.deleteAllByCartId(id);
		cart.getCartItems().clear();
		cartRepository.deleteById(id);
		
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCart(id);
		return cart.getTotalAmount();
	}
	
	@Override
	public Cart initializeNewCart(User user) {
	    return Optional.ofNullable(getCartByUserId(user.getId()))
	    		.orElseGet(() -> {
	    			Cart cart = new Cart();
	    			cart.setUser(user);
	    			return cartRepository.save(cart);
	    		});
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}

	
}
