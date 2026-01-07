package com.foodtech.kitchen.domain.model;

import java.util.List;

public class Task {
    private final Station station;
    private final List<Product> products;

    public Task(Station station, List<Product> products) {
        this.station = station;
        this.products = products;
    }

    public Station getStation() {
        return station;
    }

    public List<Product> getProducts() {
        return products;
    }
}
