package com.lucileudo.ecommerce.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucileudo.ecommerce.exceptions.BusinessException;
import com.lucileudo.ecommerce.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public Product findById(UUID id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
	}

	public Product create(Product product) {
		if (productRepository.existsByName(product.getName())) {
			throw new BusinessException("Produto já cadastrado");
		}

		return productRepository.save(product);
	}

	@Transactional
	public Product update(UUID id, Product data) {
		var product = findById(id);

		if (productRepository.existsByNameAndIdNot(data.getName(), id)) {
			throw new BusinessException("Já existe produto com esse nome");
		}

		product.setName(data.getName());
		product.setDescription(data.getDescription());
		product.setPrice(data.getPrice());
		product.setStockQuantity(data.getStockQuantity());
		product.setCategory(data.getCategory());

		return productRepository.save(product);
	}

	@Transactional
	public void delete(UUID id) {
		var product = findById(id);

		productRepository.delete(product);
	}
}