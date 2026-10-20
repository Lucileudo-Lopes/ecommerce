package com.lucileudo.ecommerce.category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, UUID id);
}