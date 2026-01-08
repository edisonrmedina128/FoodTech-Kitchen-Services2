package com.foodtech.kitchen.infrastructure.rest.mapper;

import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.domain.model.ProductType;
import com.foodtech.kitchen.infrastructure.rest.dto.CreateOrderRequest;
import com.foodtech.kitchen.infrastructure.rest.dto.ProductRequest;

import java.util.List;
import java.util.stream.Collectors;

//HUMAN REVIEW: Eliminada "inappropriate intimacy" con Map. Ahora usa ProductRequest tipado.
public class OrderMapper {

    public static Order toDomain(CreateOrderRequest request) {
        List<Product> products = request.products().stream()
            .map(OrderMapper::mapProduct)
            .collect(Collectors.toList());
        
        return new Order(request.tableNumber(), products);
    }

    private static Product mapProduct(ProductRequest productRequest) {
        if (productRequest.name() == null || productRequest.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (productRequest.type() == null || productRequest.type().trim().isEmpty()) {
            throw new IllegalArgumentException("Product type is required");
        }
        
        try {
            ProductType type = ProductType.valueOf(productRequest.type());
            return new Product(productRequest.name(), type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product type: " + productRequest.type());
        }
    }
}