package com.foodtech.kitchen.infrastructure.config;

import com.foodtech.kitchen.application.ports.in.ProcessOrderPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.application.usecases.ProcessOrderUseCase;
import com.foodtech.kitchen.domain.services.CommandFactory;
import com.foodtech.kitchen.domain.services.TaskDecomposer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public CommandFactory commandFactory() {
        return new CommandFactory();
    }

    @Bean
    public TaskDecomposer taskDecomposer(CommandFactory commandFactory) {
        return new TaskDecomposer(commandFactory);
    }

    @Bean
    public ProcessOrderPort processOrderPort(
        TaskDecomposer taskDecomposer, 
        TaskRepository taskRepository
    ) {
        return new ProcessOrderUseCase(taskDecomposer, taskRepository);
    }
}