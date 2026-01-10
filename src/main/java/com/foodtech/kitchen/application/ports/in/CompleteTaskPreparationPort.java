package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.Task;

public interface CompleteTaskPreparationPort {
    Task execute(Long taskId);
}
