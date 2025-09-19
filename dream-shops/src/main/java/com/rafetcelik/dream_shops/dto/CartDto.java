package com.rafetcelik.dream_shops.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;

@Data
public class CartDto {
	
	private Long id;
	
	private Set<CartItemDto> cartItems;
	
	private BigDecimal totalAmount;
}
