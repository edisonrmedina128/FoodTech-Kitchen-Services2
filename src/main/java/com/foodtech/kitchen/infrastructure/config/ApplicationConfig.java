package com.foodtech.kitchen.infrastructure.config;

import com.foodtech.kitchen.application.ports.in.ProcessOrderPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.application.usecases.ProcessOrderUseCase;
import com.foodtech.kitchen.domain.services.OrderValidator;
import com.foodtech.kitchen.domain.services.ProductStationMapper;
import com.foodtech.kitchen.domain.services.TaskDecomposer;
import com.foodtech.kitchen.domain.services.TaskFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//HUMAN REVIEW: Configuré beans para todas las clases de servicio separadas.
//Cada bean tiene una responsabilidad clara y se inyectan correctamente en TaskDecomposer.
@Configuration
public class ApplicationConfig {

    @Bean
    public OrderValidator orderValidator() {
        return new OrderValidator();
    }

    @Bean
    public ProductStationMapper productStationMapper() {
        return new ProductStationMapper();
    }

    @Bean
    public TaskFactory taskFactory() {
        return new TaskFactory();
    }

    @Bean
    public com.foodtech.kitchen.domain.services.CommandFactory commandFactory() {
        return new com.foodtech.kitchen.domain.services.CommandFactory();
    }

    @Bean
    public TaskDecomposer taskDecomposer(OrderValidator orderValidator,
                                         ProductStationMapper productStationMapper,
                                         TaskFactory taskFactory) {
        return new TaskDecomposer(orderValidator, productStationMapper, taskFactory);
    }

    @Bean
    public ProcessOrderPort processOrderPort(
        TaskDecomposer taskDecomposer, 
        TaskRepository taskRepository,
        com.foodtech.kitchen.domain.services.CommandFactory commandFactory,
        com.foodtech.kitchen.application.ports.out.CommandExecutor commandExecutor
    ) {
        return new ProcessOrderUseCase(taskDecomposer, taskRepository, commandFactory, commandExecutor);
    }
}