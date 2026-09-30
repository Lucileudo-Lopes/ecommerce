package com.lucileudo.ecommerce.order;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucileudo.ecommerce.user.User;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	List<Order> findByCustomer(User customer);

}
