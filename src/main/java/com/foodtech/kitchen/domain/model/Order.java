package com.foodtech.kitchen.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String tableNumber;
    private final List<Product> products;

    public Order(String tableNumber, List<Product> products) {
        this.tableNumber = tableNumber;
        this.products = new ArrayList<>(products);
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
