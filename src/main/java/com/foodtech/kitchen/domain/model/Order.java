package com.foodtech.kitchen.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    
    private final Long id;
    private final String tableNumber;
    private final List<Product> products;

    public Order(Long id, String tableNumber, List<Product> products) {
        validate(id, tableNumber, products);
        
        this.id = id != null ? id : ID_GENERATOR.getAndIncrement();
        this.tableNumber = tableNumber;
        this.products = new ArrayList<>(products);
    }

    private void validate(Long id, String tableNumber, List<Product> products) {
        // id can be null for new orders (not yet persisted)
        if (tableNumber == null || tableNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Table number cannot be null or empty");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products list cannot be null or empty");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
