package com.foodtech.kitchen.domain.model;

public class Product {
    private final String name;
    private final ProductType type;

    public Product(String name, ProductType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }
}
