# DOMAIN_MAP (Mapa de Dominio)

Fecha: 2026-02-24  
Repositorio/Servicio: FoodTech Kitchen Services

## 1) Propósito

Este documento describe el dominio actual observado en el código, con foco en:
- Conceptos del negocio (entidades, estados, invariantes).
- Relación entre conceptos (órdenes, tareas, productos, estaciones).
- Servicios de dominio existentes y su rol.

No incluye componentes no existentes ni asume integraciones externas.

---

## 2) Ubiicación del dominio (paquetes)

- Modelos (entidades del dominio): src/main/java/com/foodtech/kitchen/domain/model
- Servicios de dominio: src/main/java/com/foodtech/kitchen/domain/services
- Commands (simulación de preparación por estación): src/main/java/com/foodtech/kitchen/domain/commands

---

## 3) Conceptos principales del dominio

### 3.1 Order (Orden)

Representa una orden de cocina asociada a una mesa y a un conjunto de productos.

Atributos principales (observados):
- id (Long, puede ser null al crear)
- tableNumber (String)
- products (List<Product>)
- status (OrderStatus)

Estados (OrderStatus):
- CREATED
- IN_PROGRESS
- COMPLETED
- INVOICED

Comportamiento relevante:
- markInProgress(): transiciona CREATED → IN_PROGRESS (si aplica)
- markCompleted(): transiciona a COMPLETED
- markInvoiced(): transiciona COMPLETED → INVOICED

Invariantes/validaciones observadas:
- tableNumber no puede ser null/vacío
- products no puede ser null/vacío

Notas:
- La orden se reconstruye con factories estáticas (reconstruct) para soportar persistencia.

Referencia: src/main/java/com/foodtech/kitchen/domain/model/Order.java

---

### 3.2 Task (Tarea)

Representa una tarea de preparación ejecutable en una estación (BAR/HOT_KITCHEN/COLD_KITCHEN) asociada a una orden.

Atributos principales (observados):
- id (Long, puede ser null al crear)
- orderId (Long)
- station (Station)
- tableNumber (String)
- products (List<Product>)
- createdAt (LocalDateTime)
- status (TaskStatus)
- startedAt (LocalDateTime, nullable)
- completedAt (LocalDateTime, nullable)

Estados (TaskStatus):
- PENDING
- IN_PREPARATION
- COMPLETED

Comportamiento relevante:
- start(): PENDING → IN_PREPARATION (y set startedAt)
- complete(): IN_PREPARATION → COMPLETED (y set completedAt)

Invariantes/validaciones observadas:
- orderId, station, tableNumber, products, createdAt no pueden ser null
- products no puede ser vacío

Referencia: src/main/java/com/foodtech/kitchen/domain/model/Task.java

---

### 3.3 Product (Producto)

Elemento de la orden que determina estación de preparación a través de su tipo.

Atributos principales:
- name (String)
- type (ProductType)

Validaciones observadas:
- name no puede ser null/vacío
- type no puede ser null

Referencia: src/main/java/com/foodtech/kitchen/domain/model/Product.java

---

### 3.4 Station (Estación)

Enum que representa el lugar de preparación:
- BAR
- HOT_KITCHEN
- COLD_KITCHEN

Referencia: src/main/java/com/foodtech/kitchen/domain/model/Station.java

---

### 3.5 ProductType (Tipo de producto) y mapeo a estación

Enum con mapeo explícito a Station (observado):
- DRINK → BAR
- HOT_DISH → HOT_KITCHEN
- COLD_DISH → COLD_KITCHEN

Esto permite agrupar productos por estación sin un mapper adicional.

Referencia: src/main/java/com/foodtech/kitchen/domain/model/ProductType.java

---

## 4) Servicios de dominio (responsabilidades)

### 4.1 OrderValidator

Rol: validar reglas básicas de la orden antes de descomponerla en tareas.

Paquete: src/main/java/com/foodtech/kitchen/domain/services/OrderValidator.java

### 4.2 TaskDecomposer

Rol: descomponer una Order en una lista de Task agrupando productos por Station.

Hechos observados:
- Agrupa productos por `product.getType().getStation()`.
- Orquesta validación (OrderValidator) y creación (TaskFactory).

Referencia: src/main/java/com/foodtech/kitchen/domain/services/TaskDecomposer.java

### 4.3 TaskFactory

Rol: construir instancias Task a partir de productos agrupados por estación.

Hecho observado:
- Usa un mismo timestamp `now` para tasks creadas en la misma descomposición.

Referencia: src/main/java/com/foodtech/kitchen/domain/services/TaskFactory.java

### 4.4 CommandFactory + Strategy (selección de comando por estación)

Rol: seleccionar el Command adecuado según la Station, sin switch-case rígido.

Hechos observados:
- Interfaz `CommandStrategy` con `supports(Station)` y `createCommand(List<Product>)`.
- `CommandFactory` recibe una lista de estrategias y elige la primera compatible.

Estrategias actuales (observadas):
- PrepareDrinkStrategy (BAR)
- PrepareHotDishStrategy (HOT_KITCHEN)
- PrepareColdDishStrategy (COLD_KITCHEN)

Referencias:
- src/main/java/com/foodtech/kitchen/domain/services/CommandStrategy.java
- src/main/java/com/foodtech/kitchen/domain/services/CommandFactory.java

### 4.5 OrderStatusCalculator

Rol: derivar un estado (tipo TaskStatus) a partir del estado persistido de la orden y/o del conjunto de tasks.

Hechos observados:
- Si existe estado persistido (OrderStatus), lo mapea a TaskStatus.
- Si no existe, calcula basado en tasks:
  - si todas COMPLETED → COMPLETED
  - si alguna iniciada → IN_PREPARATION
  - si ninguna iniciada → PENDING

Referencia: src/main/java/com/foodtech/kitchen/domain/services/OrderStatusCalculator.java

---

## 5) Commands (simulación de ejecución)

Existe un conjunto de comandos de dominio que simulan preparación por estación:
- PrepareDrinkCommand
- PrepareHotDishCommand
- PrepareColdDishCommand

Hecho observado:
- Ejecutan lógica de simulación (incluyendo sleeps y prints) al invocar `execute()`.

Paquete: src/main/java/com/foodtech/kitchen/domain/commands

---

## 6) Eventos de aplicación relacionados al dominio (Outbox)

El sistema incluye una estructura de Outbox en la capa application:
- OutboxEvent (con status, attempts, timestamps, lastError)
- OutboxEventStatus

Hecho observado:
- El caso de uso de solicitud de factura persiste un evento "OrderInvoiceRequested".
- No se observa publicador/worker/scheduler para procesar el Outbox.

Referencias:
- src/main/java/com/foodtech/kitchen/application/outbox/OutboxEvent.java
- src/main/java/com/foodtech/kitchen/application/usecases/RequestOrderInvoiceUseCase.java

---

## 7) Límites y dependencias relevantes para el dominio

- El dominio (domain/*) se mantiene mayormente independiente de frameworks.
- La orquestación de cambios de estado y persistencia ocurre en application/usecases.
- La ejecución asíncrona está abstraída por el puerto AsyncCommandDispatcher (ubicado actualmente en domain/ports/out) e implementada en infraestructura.

---

## 8) Ambigüedades / aspectos no presentes

- No se observan módulos separados por subdominios (p. ej. orders/tasks) a nivel de paquetes; el dominio está centralizado por tipo (model/services/commands).
- No se observa un publicador de Outbox (scheduler/worker/listener); el mapa refleja solo persistencia del evento.

---

Fin.
