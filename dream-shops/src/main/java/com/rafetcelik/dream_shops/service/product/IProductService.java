package com.rafetcelik.dream_shops.service.product;

import java.util.List;

import com.rafetcelik.dream_shops.dto.ProductDto;
import com.rafetcelik.dream_shops.model.Product;
import com.rafetcelik.dream_shops.request.AddProductRequest;
import com.rafetcelik.dream_shops.request.ProductUpdateRequest;

public interface IProductService {
	
	Product addProduct(AddProductRequest request);
	
	Product getProductById(Long id);
	
	void deleteProductById(Long id);
	
	Product updateProduct(ProductUpdateRequest request, Long productId);
	
	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(String category);
	
	List<Product> getProductsByBrand(String brand);
	
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	
	List<Product> getProductsByName(String name);
	
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String brand, String name);

	ProductDto convertToDto(Product product);

	List<ProductDto> getConvertedProducts(List<Product> products);
}
