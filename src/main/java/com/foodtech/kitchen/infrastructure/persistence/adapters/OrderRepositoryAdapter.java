package com.foodtech.kitchen.infrastructure.persistence.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.infrastructure.persistence.jpa.OrderJpaRepository;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter {

    private final OrderJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public OrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = new ObjectMapper();
    }

    public OrderEntity save(Order order) {
        OrderEntity entity = toEntity(order);
        return jpaRepository.save(entity);
    }

    private OrderEntity toEntity(Order order) {
        try {
            String productsJson = objectMapper.writeValueAsString(
                order.getProducts().stream()
                    .map(p -> new ProductDto(p.getName(), p.getType().name()))
                    .collect(Collectors.toList())
            );

            return OrderEntity.builder()
                .tableNumber(order.getTableNumber())
                .productsJson(productsJson)
                .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting order to entity", e);
        }
    }

    private record ProductDto(String name, String type) {}
}