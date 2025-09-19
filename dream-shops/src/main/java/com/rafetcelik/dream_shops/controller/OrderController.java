package com.rafetcelik.dream_shops.controller;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.dream_shops.dto.OrderDto;
import com.rafetcelik.dream_shops.exception.ResourceNotFoundException;
import com.rafetcelik.dream_shops.model.Order;
import com.rafetcelik.dream_shops.response.ApiResponse;
import com.rafetcelik.dream_shops.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
	
	private final IOrderService orderService;
	
	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(Long userId) {
		try {
			Order order = orderService.placeOrder(userId);
			OrderDto orderDto = orderService.convertToDto(order);
			return ResponseEntity.ok(new ApiResponse("Ürün siparişi başarılı!", orderDto));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Hata!", e.getMessage()));
		}
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
		try {
			OrderDto order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Ürün başarıyla getirildi!", order));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Bir aksilik oldu!", e.getMessage()));
		}
	}
	
	@GetMapping("/{userId}/orders")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
		try {
			List<OrderDto> orders = orderService.getUserOrders(userId);
			return ResponseEntity.ok(new ApiResponse("Kullanıcıya ait siparişler getirildi!", orders));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Bir aksilik oldu!", e.getMessage()));
		}
	}
}
