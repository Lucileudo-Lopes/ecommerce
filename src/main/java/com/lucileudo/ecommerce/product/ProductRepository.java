package com.lucileudo.ecommerce.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, UUID id);
}
