package com.rafetcelik.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.dream_shops.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByName(String name);

	boolean existsByName(String name);

}
