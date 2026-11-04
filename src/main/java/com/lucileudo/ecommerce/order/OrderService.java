package com.lucileudo.ecommerce.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucileudo.ecommerce.exceptions.BusinessException;
import com.lucileudo.ecommerce.exceptions.ResourceNotFoundException;
import com.lucileudo.ecommerce.product.Product;
import com.lucileudo.ecommerce.product.ProductRepository;
import com.lucileudo.ecommerce.user.User;
import com.lucileudo.ecommerce.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	@Transactional
	public Order create(String userName, List<OrderItemRequestDTO> itemsRequest) {
		User customer = userRepository.findByUserName(userName)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

		if (itemsRequest == null || itemsRequest.isEmpty()) {
			throw new BusinessException("O pedido deve ter pelo menos um item");
		}

		Order order = new Order();
		order.setCustomer(customer);
		order.setStatus(OrderStatus.PENDENTE);

		List<OrderItem> items = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		for (OrderItemRequestDTO itemRequest : itemsRequest) {

			if (itemRequest.quantity() == null || itemRequest.quantity() < 1) {
				throw new BusinessException("A quantidade deve ser maior que zero");
			}

			Product product = productRepository.findById(itemRequest.productId())
					.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

			if (product.getStockQuantity() < itemRequest.quantity()) {
				throw new BusinessException("Estoque insuficiente para o produto: " + product.getName());
			}

			product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
			productRepository.save(product);

			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setProduct(product);
			item.setQuantity(itemRequest.quantity());
			item.setUnitPrice(product.getPrice());
			items.add(item);

			total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
		}

		order.setItems(items);
		order.setTotal(total);

		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<Order> findByCustomer(String username) {
		User customer = userRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

		return orderRepository.findByCustomer(customer);
	}

	@Transactional(readOnly = true)
	public Order findById(UUID id, String username, boolean isAdmin) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

		if (!isAdmin && !order.getCustomer().getUserName().equals(username)) {
			throw new ResourceNotFoundException("Pedido não encontrado");
		}

		return order;
	}

	@Transactional(readOnly = true)
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order updateStatus(UUID orderId, OrderStatus status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

		if (order.getStatus() == OrderStatus.CANCELADO) {
			throw new BusinessException("Pedido cancelado não pode ter o status alterado");
		}

		if (status == OrderStatus.CANCELADO) {
			for (OrderItem item : order.getItems()) {
				Product product = item.getProduct();
				product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
				productRepository.save(product);
			}
		}

		order.setStatus(status);

		return orderRepository.save(order);
	}
}