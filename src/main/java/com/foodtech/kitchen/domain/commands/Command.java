package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.domain.model.Station;

import java.util.List;

public interface Command {
    void execute();
    Station getStation();
    List<Product> getProducts();
}