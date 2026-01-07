package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrepareColdDishCommandTest {

    @Test
    @DisplayName("Should create cold dish command with correct station")
    void shouldCreateColdDishCommandWithCorrectStation() {
        // Given
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        List<Product> products = List.of(salad);

        // When
        PrepareColdDishCommand command = new PrepareColdDishCommand(products);

        // Then
        assertEquals(Station.COLD_KITCHEN, command.getStation());
        assertEquals(1, command.getProducts().size());
    }

    @Test
    @DisplayName("Should execute cold dish preparation")
    void shouldExecuteColdDishPreparation() {
        // Given
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        PrepareColdDishCommand command = new PrepareColdDishCommand(List.of(salad));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }
}