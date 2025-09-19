package com.rafetcelik.dream_shops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rafetcelik.dream_shops.exception.AlreadyExistsException;
import com.rafetcelik.dream_shops.exception.ResourceNotFoundException;
import com.rafetcelik.dream_shops.model.Category;
import com.rafetcelik.dream_shops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
	
	private final CategoryRepository categoryRepository;
	
	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı! ID: " + id));
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
				.map(categoryRepository::save)
				.orElseThrow(() -> new AlreadyExistsException("Bu isimde bir kategori zaten mevcut!"));
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		return Optional.ofNullable(getCategoryById(id))
				.map(oldCategory -> {
					oldCategory.setName(category.getName());
					return categoryRepository.save(oldCategory);
				}) .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı! ID: " + id));
	}

	@Override
	public void deleteCategoryById(Long id) {
		categoryRepository.findById(id)
				.ifPresentOrElse(categoryRepository::delete, 
						() -> {throw new ResourceNotFoundException("Kategori bulunamadı! ID: " + id);});
	}

}
