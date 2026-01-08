package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.*;

import java.util.*;

//HUMAN REVIEW: Separé responsabilidades en clases dedicadas (OrderValidator, ProductStationMapper, TaskFactory).
//Ahora TaskDecomposer solo orquesta el flujo, cumpliendo SRP. Cada clase tiene una única razón de cambio.
public class TaskDecomposer {

    private final OrderValidator orderValidator;
    private final ProductStationMapper stationMapper;
    private final TaskFactory taskFactory;

    public TaskDecomposer(OrderValidator orderValidator,
            ProductStationMapper stationMapper,
            TaskFactory taskFactory) {
        this.orderValidator = orderValidator;
        this.stationMapper = stationMapper;
        this.taskFactory = taskFactory;
    }

    public List<Task> decompose(Order order) {
        orderValidator.validate(order);

        Map<Station, List<Product>> productsByStation = groupProductsByStation(order);

        return taskFactory.createTasks(productsByStation);
    }

    private Map<Station, List<Product>> groupProductsByStation(Order order) {
        Map<Station, List<Product>> productsByStation = new HashMap<>();

        for (Product product : order.getProducts()) {
            Station station = stationMapper.mapToStation(product.getType());
            productsByStation
                    .computeIfAbsent(station, k -> new ArrayList<>())
                    .add(product);
        }

        return productsByStation;
    }
}