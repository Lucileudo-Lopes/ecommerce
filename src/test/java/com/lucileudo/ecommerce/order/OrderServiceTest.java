package com.lucileudo.ecommerce.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lucileudo.ecommerce.exceptions.BusinessException;
import com.lucileudo.ecommerce.product.Product;
import com.lucileudo.ecommerce.product.ProductRepository;
import com.lucileudo.ecommerce.user.User;
import com.lucileudo.ecommerce.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private OrderService orderService;

	private Product product;
	private User user;

	@BeforeEach
	void setUp() {
		product = new Product();
		product.setId(UUID.randomUUID());
		product.setName("Teclado Mecanico");
		product.setPrice(BigDecimal.valueOf(250.00));
		product.setStockQuantity(5);

		user = new User();
		user.setUserName("lucileudo");
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueForInsuficiente() {

		when(userRepository.findByUserName("lucileudo")).thenReturn(Optional.of(user));
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

		var itemRequest = new OrderItemRequestDTO(product.getId(), 10);

		assertThrows(BusinessException.class, () -> orderService.create("lucileudo", List.of(itemRequest)));

		verify(orderRepository, never()).save(any());
	}

	@Test
	void deveCriarPedidoComSucessoQuandoEstoqueSuficiente() {

		when(userRepository.findByUserName("lucileudo")).thenReturn(Optional.of(user));
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		var itemRequest = new OrderItemRequestDTO(product.getId(), 2);

		var order = orderService.create("lucileudo", List.of(itemRequest));

		assertEquals(BigDecimal.valueOf(500.00), order.getTotal());
		assertEquals(OrderStatus.PENDENTE, order.getStatus());
	}
}