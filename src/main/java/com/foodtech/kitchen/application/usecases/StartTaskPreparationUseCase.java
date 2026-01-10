package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.in.StartTaskPreparationPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Task;

public class StartTaskPreparationUseCase implements StartTaskPreparationPort {

    private final TaskRepository taskRepository;

    public StartTaskPreparationUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task execute(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        
        task.start();
        
        return taskRepository.save(task);
    }
}
