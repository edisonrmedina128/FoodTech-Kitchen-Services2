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
        Task inPreparationTask = Task.reconstruct(
                taskId,
                1L,
                Station.BAR,
                "A1",
                List.of(product),
                LocalDateTime.now(),
                TaskStatus.IN_PREPARATION,
                LocalDateTime.now(),
                null
        );

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

    @Test
    void shouldThrowExceptionWhenCompletingPendingTask() {
        // Given
        Long taskId = 1L;
        Product product = new Product("Cerveza", ProductType.DRINK);
        Task pendingTask = Task.reconstruct(
                taskId,
                1L,
                Station.BAR,
                "A1",
                List.of(product),
                LocalDateTime.now(),
                TaskStatus.PENDING,
                null,
                null
        );
        // Task is PENDING, not started

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(pendingTask));

        // When & Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> useCase.execute(taskId)
        );
        assertEquals("Task must be in IN_PREPARATION status to complete", exception.getMessage());
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
