package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrepareDrinkCommandTest {

    @Test
    @DisplayName("Should create drink command with correct station")
    void shouldCreateDrinkCommandWithCorrectStation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        List<Product> products = List.of(cocaCola);

        // When
        PrepareDrinkCommand command = new PrepareDrinkCommand(products);

        // Then
        assertEquals(Station.BAR, command.getStation());
        assertEquals(1, command.getProducts().size());
        assertEquals("Coca Cola", command.getProducts().get(0).getName());
    }

    @Test
    @DisplayName("Should execute drink preparation")
    void shouldExecuteDrinkPreparation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        PrepareDrinkCommand command = new PrepareDrinkCommand(List.of(cocaCola));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    @DisplayName("Should handle multiple drinks in one command")
    void shouldHandleMultipleDrinks() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        Product sprite = new Product("Sprite", ProductType.DRINK);
        List<Product> products = List.of(cocaCola, sprite);

        // When
        PrepareDrinkCommand command = new PrepareDrinkCommand(products);

        // Then
        assertEquals(2, command.getProducts().size());
        assertEquals(Station.BAR, command.getStation());
    }
}