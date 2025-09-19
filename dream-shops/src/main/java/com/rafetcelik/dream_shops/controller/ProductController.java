package com.rafetcelik.dream_shops.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.dream_shops.dto.ProductDto;
import com.rafetcelik.dream_shops.exception.AlreadyExistsException;
import com.rafetcelik.dream_shops.exception.ResourceNotFoundException;
import com.rafetcelik.dream_shops.model.Product;
import com.rafetcelik.dream_shops.request.AddProductRequest;
import com.rafetcelik.dream_shops.request.ProductUpdateRequest;
import com.rafetcelik.dream_shops.response.ApiResponse;
import com.rafetcelik.dream_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
	
	private final IProductService productService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
		return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
	}
	
	@GetMapping("/product/{productId}/product")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
		try {
			Product product = productService.getProductById(productId);
			ProductDto productDto = productService.convertToDto(product);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", productDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
		try {
			Product theProduct = productService.addProduct(product);
			ProductDto productDto = productService.convertToDto(theProduct);
			return ResponseEntity.ok(new ApiResponse("Ürün başarıyla eklendi!", productDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/product/{productId}/update")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
		try {
			Product theProduct = productService.updateProduct(request, productId);
			ProductDto productDto = productService.convertToDto(theProduct);
			return ResponseEntity.ok(new ApiResponse("Ürün başarıyla güncellendi!", productDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProductById(productId);
			return ResponseEntity.ok(new ApiResponse("Ürün başarıyla silindi!", productId));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/products/by/brand-and-name")
	public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
		try {
			List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
			if(products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Ürünler bulunamadı!", null));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
		}
	}
	
	@GetMapping("/products/by/category-and-brand")
	public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
			if(products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Ürünler bulunamadı!", null));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Hata!", e.getMessage()));
		}
	}
	
	@GetMapping("/products/{name}/products")
	public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
		try {
			List<Product> products = productService.getProductsByName(name);
			if(products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Ürünler bulunamadı!", null));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Hata!", e.getMessage()));
		}
	}
	
	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByBrand(brand);
			if(products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Ürünler bulunamadı!", null));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Hata!", e.getMessage()));
		}
	}
	
	@GetMapping("/product/{category}/all/products")
	public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
		try {
			List<Product> products = productService.getProductsByCategory(category);
			if(products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Ürünler bulunamadı!", null));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Başarılı!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Hata", e.getMessage()));
		}
	}
	
	@GetMapping("/product/count/by-brand/and-name")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
		try {
			var productCount = productService.countProductsByBrandAndName(brand, name);
			return ResponseEntity.ok(new ApiResponse("Ürün sayısı!", productCount));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
		}
	}
}
