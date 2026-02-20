package com.foodtech.kitchen.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.foodtech.kitchen.application.ports.in.*;
import com.foodtech.kitchen.application.ports.out.CommandExecutor;
import com.foodtech.kitchen.application.ports.out.OrderRepository;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.application.usecases.*;
import com.foodtech.kitchen.domain.ports.out.AsyncCommandDispatcher;
import com.foodtech.kitchen.domain.services.*;
import com.foodtech.kitchen.infrastructure.execution.ReactorAsyncCommandDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public OrderValidator orderValidator() {
        return new OrderValidator();
    }

    @Bean
    public TaskFactory taskFactory() {
        return new TaskFactory();
    }

    @Bean
    public PrepareDrinkStrategy prepareDrinkStrategy() {
        return new PrepareDrinkStrategy();
    }

    @Bean
    public PrepareHotDishStrategy prepareHotDishStrategy() {
        return new PrepareHotDishStrategy();
    }

    @Bean
    public PrepareColdDishStrategy prepareColdDishStrategy() {
        return new PrepareColdDishStrategy();
    }

    @Bean
    public CommandFactory commandFactory(List<CommandStrategy> strategies) {
        return new CommandFactory(strategies);
    }

    @Bean
    public TaskDecomposer taskDecomposer(
            OrderValidator orderValidator,
            TaskFactory taskFactory
    ) {
        return new TaskDecomposer(orderValidator, taskFactory);
    }

    @Bean
    public OrderStatusCalculator orderStatusCalculator() {
        return new OrderStatusCalculator();
    }

    @Bean
    public ProcessOrderPort processOrderPort(
            OrderRepository orderRepository,
            TaskDecomposer taskDecomposer,
            TaskRepository taskRepository
    ) {
        return new ProcessOrderUseCase(orderRepository, taskDecomposer, taskRepository);
    }

    @Bean
    public StartTaskPreparationPort startTaskPreparationPort(
            TaskRepository taskRepository,
            OrderRepository orderRepository,
            CommandFactory commandFactory,
            AsyncCommandDispatcher asyncCommandDispatcher
    ) {
        return new StartTaskPreparationUseCase(
                taskRepository,
                orderRepository,
                commandFactory,
                asyncCommandDispatcher
        );
    }

    @Bean
    public AsyncCommandDispatcher asyncCommandDispatcher(
            CommandExecutor commandExecutor,
            TaskRepository taskRepository,
            OrderCompletionService orderCompletionService
    ) {
        return new ReactorAsyncCommandDispatcher(
                commandExecutor,
                taskRepository,
                orderCompletionService
        );
    }

    @Bean
    public GetTasksByStationPort getTasksByStationPort(
            TaskRepository taskRepository
    ) {
        return new GetTasksByStationUseCase(taskRepository);
    }

    @Bean
    public GetOrderStatusPort getOrderStatusPort(
            TaskRepository taskRepository,
            OrderRepository orderRepository,
            OrderStatusCalculator orderStatusCalculator
    ) {
        return new GetOrderStatusUseCase(taskRepository, orderRepository, orderStatusCalculator);
    }

    @Bean
    public GetCompletedOrdersPort getCompletedOrdersPort(
            OrderRepository orderRepository,
            TaskRepository taskRepository
    ) {
        return new GetCompletedOrdersUseCase(orderRepository, taskRepository);
    }

    @Bean
    public RequestOrderInvoicePort requestOrderInvoicePort(
            OrderRepository orderRepository,
            com.foodtech.kitchen.application.ports.out.OutboxEventRepository outboxEventRepository,
            InvoicePayloadBuilder payloadBuilder
    ) {
        return new RequestOrderInvoiceUseCase(orderRepository, outboxEventRepository, payloadBuilder);
    }
}