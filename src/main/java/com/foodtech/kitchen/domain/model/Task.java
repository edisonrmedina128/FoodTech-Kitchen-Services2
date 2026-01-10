package com.foodtech.kitchen.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private final Long id;
    private final Long orderId;
    private final Station station;
    private final String tableNumber;
    private final List<Product> products;
    private final LocalDateTime createdAt;
    private TaskStatus status;
    private LocalDateTime startedAt;

    public Task(Long id, Long orderId, Station station, String tableNumber, List<Product> products, LocalDateTime createdAt) {
        validate(id, orderId, station, tableNumber, products, createdAt);
        this.id = id;
        this.orderId = orderId;
        this.station = station;
        this.tableNumber = tableNumber;
        this.products = new ArrayList<>(products);
        this.createdAt = createdAt;
        this.status = TaskStatus.PENDING;
        this.startedAt = null;
    }

    private void validate(Long id, Long orderId, Station station, String tableNumber, List<Product> products, LocalDateTime createdAt) {
        // id can be null for new tasks (not yet persisted)
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (station == null) {
            throw new IllegalArgumentException("Station cannot be null");
        }
        if (tableNumber == null || tableNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Table number cannot be null or empty");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products list cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at timestamp cannot be null");
        }
    }

    public void start() {
        if (this.status != TaskStatus.PENDING) {
            throw new IllegalStateException("Task must be in PENDING status to start");
        }
        this.status = TaskStatus.IN_PREPARATION;
        this.startedAt = LocalDateTime.now();
    }

    // Factory method for reconstructing Task from persistence
    public static Task reconstruct(Long id, Long orderId, Station station, String tableNumber, 
                                   List<Product> products, LocalDateTime createdAt, 
                                   TaskStatus status, LocalDateTime startedAt) {
        Task task = new Task(id, orderId, station, tableNumber, products, createdAt);
        task.status = status;
        task.startedAt = startedAt;
        return task;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Station getStation() {
        return station;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }
}