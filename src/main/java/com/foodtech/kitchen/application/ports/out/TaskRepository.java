package com.foodtech.kitchen.application.ports.out;

import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.Station;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void saveAll(List<Task> tasks);
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByStation(Station station);
    List<Task> findAll();
}