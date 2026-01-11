package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.in.CompleteTaskPreparationPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Task;

public class CompleteTaskPreparationUseCase implements CompleteTaskPreparationPort {

    private final TaskRepository taskRepository;

    public CompleteTaskPreparationUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task execute(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        
        task.complete();
        
        return taskRepository.save(task);
    }
}
