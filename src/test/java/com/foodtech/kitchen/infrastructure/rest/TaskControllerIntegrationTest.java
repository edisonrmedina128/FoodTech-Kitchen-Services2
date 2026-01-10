package com.foodtech.kitchen.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Given - Preparar datos: 3 tareas (2 BAR, 1 HOT_KITCHEN)
        
        // Crear pedido con bebidas para BAR
        Map<String, Object> orderBar1 = Map.of(
            "tableNumber", "A1",
            "products", List.of(
                Map.of("name", "Coca Cola", "type", "DRINK")
            )
        );
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderBar1)));

        // Crear otro pedido con bebida para BAR
        Map<String, Object> orderBar2 = Map.of(
            "tableNumber", "A2",
            "products", List.of(
                Map.of("name", "Sprite", "type", "DRINK")
            )
        );
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderBar2)));

        // Crear pedido con plato caliente para HOT_KITCHEN
        Map<String, Object> orderHotKitchen = Map.of(
            "tableNumber", "B1",
            "products", List.of(
                Map.of("name", "Pizza", "type", "HOT_DISH")
            )
        );
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderHotKitchen)));
    }

    @Test
    @DisplayName("Scenario 1: Should return only tasks for specified station")
    void shouldReturnOnlyTasksForSpecifiedStation() throws Exception {
        // When - el encargado de barra consulta sus tareas
        // Then - el sistema muestra únicamente tareas de barra (verifica filtrado)
        mockMvc.perform(get("/api/tasks/station/BAR"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[*].station").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("BAR"))))
            .andExpect(jsonPath("$[0].tableNumber").exists())
            .andExpect(jsonPath("$[0].products").isArray());
    }
}
