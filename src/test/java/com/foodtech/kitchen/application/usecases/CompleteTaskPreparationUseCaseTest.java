package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.domain.model.ProductType;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteTaskPreparationUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private CompleteTaskPreparationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CompleteTaskPreparationUseCase(taskRepository);
    }

    @Test
    void shouldCompleteTaskPreparationWhenTaskIsInPreparation() {
        // Given
        Long taskId = 1L;
        Product product = new Product("Cerveza", ProductType.DRINK);
        Task inPreparationTask = new Task(
                taskId,
                1L,
                Station.BAR,
                "A1",
                List.of(product),
                LocalDateTime.now()
        );
        inPreparationTask.start(); // Set status to IN_PREPARATION

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(inPreparationTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Task result = useCase.execute(taskId);

        // Then
        assertNotNull(result);
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getCompletedAt());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }
}
