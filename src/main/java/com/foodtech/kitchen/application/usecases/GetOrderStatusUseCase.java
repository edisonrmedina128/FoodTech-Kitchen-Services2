package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.in.GetOrderStatusPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;

import java.util.List;

public class GetOrderStatusUseCase implements GetOrderStatusPort {

    private final TaskRepository taskRepository;

    public GetOrderStatusUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskStatus execute(Long orderId) {
        List<Task> tasks = taskRepository.findByOrderId(orderId);
        
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        // If any task is in preparation, order is in preparation
        boolean anyInPreparation = tasks.stream()
            .anyMatch(task -> task.getStatus() == TaskStatus.IN_PREPARATION);
        
        if (anyInPreparation) {
            return TaskStatus.IN_PREPARATION;
        }

        // If all tasks are completed, order is completed
        boolean allCompleted = tasks.stream()
            .allMatch(task -> task.getStatus() == TaskStatus.COMPLETED);
        
        if (allCompleted) {
            return TaskStatus.COMPLETED;
        }

        // Otherwise, order is pending
        return TaskStatus.PENDING;
    }
}
