package com.lucileudo.ecommerce.product;

import java.math.BigDecimal;
import com.lucileudo.ecommerce.category.Category;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_product")
public class Product {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String name;
	private String description;
	private BigDecimal price;
	private Integer stockQuantity;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
}
