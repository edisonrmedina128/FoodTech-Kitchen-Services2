package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;

import java.util.List;

public class OrderStatusCalculator {

    public TaskStatus calculateOrderStatus(List<Task> tasks) {
        validateTasks(tasks);

        if (hasAnyTaskInPreparation(tasks)) {
            return TaskStatus.IN_PREPARATION;
        }

        if (areAllTasksCompleted(tasks)) {
            return TaskStatus.COMPLETED;
        }

        return TaskStatus.PENDING;
    }

    private void validateTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate status for empty task list");
        }
    }

    private boolean hasAnyTaskInPreparation(List<Task> tasks) {
        return tasks.stream()
                .anyMatch(task -> task.getStatus() == TaskStatus.IN_PREPARATION);
    }

    private boolean areAllTasksCompleted(List<Task> tasks) {
        return tasks.stream()
                .allMatch(task -> task.getStatus() == TaskStatus.COMPLETED);
    }
}