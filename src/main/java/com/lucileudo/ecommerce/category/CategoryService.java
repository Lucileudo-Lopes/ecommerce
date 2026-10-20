package com.lucileudo.ecommerce.category;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucileudo.ecommerce.exceptions.BusinessException;
import com.lucileudo.ecommerce.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findById(UUID id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
	}

	public Category create(Category category) {
		if (categoryRepository.existsByName(category.getName())) {
			throw new BusinessException("Categoria já existe");
		}

		return categoryRepository.save(category);
	}

	@Transactional
	public Category update(UUID id, Category data) {
		var category = findById(id);

		if (categoryRepository.existsByNameAndIdNot(data.getName(), id)) {
			throw new BusinessException("Já existe outra categoria com esse nome");
		}
		category.setName(data.getName());
		return categoryRepository.save(category);
	}

	@Transactional
	public void delete(UUID id) {
		var category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

		if (category.getProducts() != null && !category.getProducts().isEmpty()) {
			throw new BusinessException("Não é possível remover categoria com produtos vinculados");

		}

		categoryRepository.delete(category);
	}
}
