package com.lucileudo.ecommerce.order;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody List<OrderItemRequestDTO> items, Authentication authentication) {
		var order = orderService.create(authentication.getName(), items);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}

	@GetMapping("/me")
	public ResponseEntity<List<Order>> findMyOrders(Authentication authentication) {
		return ResponseEntity.ok(orderService.findByCustomer(authentication.getName()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> findById(@PathVariable UUID id, Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		return ResponseEntity.ok(orderService.findById(id, authentication.getName(), isAdmin));
	}

	@GetMapping
	public ResponseEntity<List<Order>> findAll() {
		return ResponseEntity.ok(orderService.findAll());
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Order> updateStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateStatus(id, status));
	}
}