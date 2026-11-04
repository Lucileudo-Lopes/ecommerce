package com.lucileudo.ecommerce.order;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, Integer quantity) {

}
