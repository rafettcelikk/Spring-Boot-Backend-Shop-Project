package com.rafetcelik.dream_shops.request;

import java.math.BigDecimal;

import com.rafetcelik.dream_shops.model.Category;

import lombok.Data;

@Data
public class ProductUpdateRequest {

	private Long id;
	
	private String name;
	
	private String brand;
	
	private BigDecimal price;
	
	private int inventory;
	
	private String description;
	
	private Category category;
}
