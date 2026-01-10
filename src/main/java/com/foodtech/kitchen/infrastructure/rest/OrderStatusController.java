package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.ports.in.GetOrderStatusPort;
import com.foodtech.kitchen.domain.model.TaskStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderStatusController {

    private final GetOrderStatusPort getOrderStatusPort;

    public OrderStatusController(GetOrderStatusPort getOrderStatusPort) {
        this.getOrderStatusPort = getOrderStatusPort;
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<Map<String, String>> getOrderStatus(@PathVariable Long orderId) {
        TaskStatus status = getOrderStatusPort.execute(orderId);
        return ResponseEntity.ok(Map.of(
            "orderId", orderId.toString(),
            "status", status.name()
        ));
    }
}
