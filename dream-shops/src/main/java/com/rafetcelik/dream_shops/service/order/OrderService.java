package com.rafetcelik.dream_shops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rafetcelik.dream_shops.dto.OrderDto;
import com.rafetcelik.dream_shops.enums.OrderStatus;
import com.rafetcelik.dream_shops.exception.ResourceNotFoundException;
import com.rafetcelik.dream_shops.model.Cart;
import com.rafetcelik.dream_shops.model.Order;
import com.rafetcelik.dream_shops.model.OrderItem;
import com.rafetcelik.dream_shops.model.Product;
import com.rafetcelik.dream_shops.repository.OrderRepository;
import com.rafetcelik.dream_shops.repository.ProductRepository;
import com.rafetcelik.dream_shops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
	
	private final OrderRepository orderRepository;
	
	private final ProductRepository productRepository;
	
	private final ICartService cartService;
	
	private final ModelMapper modelMapper;
	
	@Override
	public Order placeOrder(Long userId) {
		Cart cart = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		
		List<OrderItem> orderItems = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItems));
		order.setTotalAmount(calculateTotalAmount(orderItems));
		
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		
		return savedOrder;
	}
	
	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return order;
	}
	
	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getCartItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return new OrderItem(
					order,
					product,
					cartItem.getQuantity(),
					cartItem.getUnitPrice());
		}).toList();
	}
	
	private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
		return orderItems
				.stream()
				.map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal :: add); 
	}
	
	@Override
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.map(this :: convertToDto)
				.orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı!"));
	}
	
	@Override
	public List<OrderDto> getUserOrders(Long userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		return orders.stream().map(this :: convertToDto).toList();
	}
	
	@Override
	public OrderDto convertToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}
}
