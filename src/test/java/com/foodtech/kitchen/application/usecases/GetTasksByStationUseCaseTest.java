package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.domain.model.*;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTasksByStationUseCaseTest {

    private GetTasksByStationUseCase useCase;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        useCase = new GetTasksByStationUseCase(taskRepository);
    }

    @Test
    @DisplayName("Should get tasks for specific station")
    void shouldGetTasksForSpecificStation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        Task barTask = new Task(Station.BAR, List.of(cocaCola));
        
        when(taskRepository.findByStation(Station.BAR))
            .thenReturn(List.of(barTask));

        // When
        List<Task> tasks = useCase.execute(Station.BAR);

        // Then
        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
        verify(taskRepository, times(1)).findByStation(Station.BAR);
    }

    @Test
    @DisplayName("Should return empty list when no tasks for station")
    void shouldReturnEmptyListWhenNoTasksForStation() {
        // Given
        when(taskRepository.findByStation(Station.HOT_KITCHEN))
            .thenReturn(List.of());

        // When
        List<Task> tasks = useCase.execute(Station.HOT_KITCHEN);

        // Then
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByStation(Station.HOT_KITCHEN);
    }

    @Test
    @DisplayName("Should only return tasks for requested station")
    void shouldOnlyReturnTasksForRequestedStation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        Product sprite = new Product("Sprite", ProductType.DRINK);
        Task barTask1 = new Task(Station.BAR, List.of(cocaCola));
        Task barTask2 = new Task(Station.BAR, List.of(sprite));
        
        when(taskRepository.findByStation(Station.BAR))
            .thenReturn(List.of(barTask1, barTask2));

        // When
        List<Task> tasks = useCase.execute(Station.BAR);

        // Then
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(task -> task.getStation() == Station.BAR));
    }
}