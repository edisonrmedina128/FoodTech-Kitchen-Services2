package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.ports.in.ProcessOrderPort;
import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.infrastructure.rest.dto.CreateOrderRequest;
import com.foodtech.kitchen.infrastructure.rest.dto.CreateOrderResponse;
import com.foodtech.kitchen.infrastructure.rest.mapper.OrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProcessOrderPort processOrderPort;

    public OrderController(ProcessOrderPort processOrderPort) {
        this.processOrderPort = processOrderPort;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            // Map DTO to domain
            Order order = OrderMapper.toDomain(request);
            
            // Execute use case
            List<Task> tasks = processOrderPort.execute(order);
            
            // Build response
            CreateOrderResponse response = new CreateOrderResponse(
                order.getTableNumber(),
                tasks.size(),
                "Order processed successfully"
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> error = Map.of(
                "error", e.getMessage(),
                "message", "Validation failed"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (Exception e) {
            Map<String, String> error = Map.of(
                "error", "Internal server error",
                "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}