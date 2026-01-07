package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrepareHotDishCommandTest {

    @Test
    @DisplayName("Should create hot dish command with correct station")
    void shouldCreateHotDishCommandWithCorrectStation() {
        // Given
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        List<Product> products = List.of(pizza);

        // When
        PrepareHotDishCommand command = new PrepareHotDishCommand(products);

        // Then
        assertEquals(Station.HOT_KITCHEN, command.getStation());
        assertEquals(1, command.getProducts().size());
    }

    @Test
    @DisplayName("Should execute hot dish preparation")
    void shouldExecuteHotDishPreparation() {
        // Given
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        PrepareHotDishCommand command = new PrepareHotDishCommand(List.of(pizza));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }
}