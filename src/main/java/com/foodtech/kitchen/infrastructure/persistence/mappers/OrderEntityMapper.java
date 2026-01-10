package com.foodtech.kitchen.infrastructure.persistence.mappers;

import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.OrderEntity;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEntityMapper {

    // ✅ NO más ObjectMapper, NO más try-catch, NO más JSON
    public OrderEntity toEntity(Order order) {
        List<ProductEntity> products = order.getProducts().stream()
                .map(this::toProductEntity)
                .collect(Collectors.toList());

        return OrderEntity.builder()
                .tableNumber(order.getTableNumber())
                .products(products)
                .build();
    }

    private ProductEntity toProductEntity(Product product) {
        return ProductEntity.builder()
                .name(product.getName())
                .type(product.getType())
                .build();
    }

    // Si necesitas mapear de vuelta a dominio
    public Order toDomain(OrderEntity entity) {
        List<Product> products = entity.getProducts().stream()
                .map(this::toProduct)
                .collect(Collectors.toList());

        return new Order(entity.getTableNumber(), products);
    }

    private Product toProduct(ProductEntity entity) {
        return new Product(entity.getName(), entity.getType());
    }
}