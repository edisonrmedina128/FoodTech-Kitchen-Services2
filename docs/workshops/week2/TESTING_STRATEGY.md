# Testing Strategy — JWT Authentication Feature

## 1. Propósito
Definir una estrategia de pruebas verificable y repetible para el feature de autenticación JWT en la rama `feature/jwt-auth`, garantizando que el registro, el inicio de sesión, la emisión/validación de tokens y el acceso a endpoints protegidos se comporten de forma correcta bajo distintas condiciones.

## 2. Alcance

### 2.1 Incluye
- Registro de usuario (validaciones de email, username y password; prevención de duplicados).
- Inicio de sesión mediante identificador por **username** o **email**.
- Generación de token JWT tras autenticación exitosa.
- Validación de expiración del token.
- Manejo de tokens malformados.
- Protección de endpoints que requieren autenticación:
  - Rechazo cuando falta token.
  - Acceso cuando el token es válido.

### 2.2 Excluye
- Autorización basada en roles/permisos.
- Refresh tokens, rotación de tokens y “remember me”.
- Revocación/blacklist de tokens, cierre de sesión global e invalidación server-side.
- Verificación de email, recuperación/cambio de contraseña, MFA/2FA.
- Integraciones externas (OAuth2/social login, proveedores de identidad).
- Rate limiting, protección contra fuerza bruta, CAPTCHA.
- Auditoría/telemetría, trazas de seguridad, cumplimiento normativo.
- Hardening adicional de transporte/infra (TLS, WAF) fuera del código.

## 3. Niveles de prueba

### A) Unit Tests (Dominio + Aplicación)
Objetivo: validar reglas, invariantes y decisiones de negocio sin infraestructura real.

- **Dominio**
  - Reglas de validación de email.
  - Reglas de robustez de password (longitud mínima y composición).
  - Reglas de unicidad (a nivel de políticas/casos de uso, no de base de datos).
  - Estados del usuario relevantes para autenticación (p. ej., activo/inactivo).

- **Aplicación**
  - Orquestación de casos de uso de registro e inicio de sesión.
  - Respuestas ante duplicados (email/username) y credenciales inválidas.
  - Delegación correcta hacia componentes de seguridad (generación/validación de token) mediante dobles de prueba.

Criterios:
- Rápidas, deterministas, sin Spring context.
- Aisladas por contrato (mocks/fakes donde aplique).

### B) Integration Tests (SpringBootTest + H2 en memoria)
Objetivo: validar el comportamiento de extremo a extremo dentro del proceso, incluyendo wiring de Spring, persistencia y capa web.

- Se levantan componentes reales del feature en un contexto de prueba con `@SpringBootTest`.
- Persistencia sobre **H2 en memoria** para verificar:
  - Inserción y restricciones (unicidad y consistencia).
  - Flujos completos de registro/login con datos reales.
- Pruebas sobre endpoints y seguridad:
  - Acceso a endpoint protegido sin token.
  - Acceso con token válido emitido por el flujo de login.

Criterios:
- Ejecutables en CI sin dependencias externas.
- Limpieza/aislamiento por caso (reinicio de contexto o limpieza de BD) para evitar acoplamientos entre pruebas.

## 4. Escenarios de prueba (definidos)
Esta lista es normativa para el feature y debe estar cubierta por Unit o Integration según corresponda:

- Register success
- Register duplicate email
- Register duplicate username
- Invalid email format
- Weak password (menos de 6, sin número, sin letra)
- Login success (username)
- Login success (email)
- Login wrong password
- Login inactive user
- Token generation
- Token expiration validation
- Token malformed
- Protected endpoint without token
- Protected endpoint with valid token

## 5. Asignación sugerida por nivel

### Unit Tests
- Invalid email format
- Weak password (menos de 6, sin número, sin letra)
- Login wrong password
- Login inactive user
- Token generation
- Token expiration validation
- Token malformed

### Integration Tests
- Register success
- Register duplicate email
- Register duplicate username
- Login success (username)
- Login success (email)
- Protected endpoint without token
- Protected endpoint with valid token

Nota: algunos escenarios pueden duplicarse en ambos niveles cuando el riesgo lo justifique (p. ej., reglas críticas en Unit y además verificación end-to-end en Integration).

## 6. Estrategia TDD paso a paso (RED → GREEN → REFACTOR)
Aplicar este ciclo por cada escenario de la sección 4:

1) RED — Especificar el comportamiento
- Elegir un único escenario objetivo.
- Definir el resultado esperado de forma observable (éxito, rechazo, estado de usuario, validez del token, acceso denegado/permitido).
- Escribir la prueba mínima que falla por la razón correcta.

2) GREEN — Implementación mínima
- Implementar la mínima lógica necesaria para que la prueba pase.
- Evitar optimizaciones prematuras.
- Mantener el foco: no implementar funcionalidades fuera del escenario.

3) REFACTOR — Diseño y mantenibilidad
- Eliminar duplicación en pruebas y producción.
- Mejorar nombres, separar responsabilidades y reforzar invariantes.
- Mantener el comportamiento: ejecutar el suite para confirmar que todo sigue en verde.

4) Consolidación
- Asegurar que el escenario queda cubierto en el nivel adecuado (Unit/Integration).
- Si el bug/riesgo es de integración, añadir la prueba de integración correspondiente incluso si ya existe una unitaria.

## 7. Uso de H2 en perfil de test
- Las pruebas de integración deben ejecutar el feature con un **perfil de test** dedicado, de manera que la configuración de persistencia apunte a **H2 en memoria**.
- El objetivo del perfil es:
  - Evitar depender de bases externas.
  - Mantener datos efímeros por ejecución.
  - Garantizar repetibilidad en local y en CI.
- La estrategia de aislamiento recomendada es:
  - Inicializar el estado necesario por prueba.
  - Asegurar limpieza entre pruebas (transacciones de test y/o reinicio controlado del estado) para que la unicidad (email/username) se valide de forma consistente.

## 8. Exclusiones explícitas (recordatorio)
Para evitar crecimiento de alcance dentro del feature JWT actual, quedan fuera:
- Roles/permisos.
- Refresh token.
- Revocación/blacklist.
- MFA/2FA.
- Recuperación/verificación de email.
- Integraciones OAuth2/social.
- Rate limiting/anti brute-force.

## 9. Criterios de aceptación de pruebas
- Todos los escenarios enumerados en la sección 4 están cubiertos.
- Unit tests cubren reglas de negocio y validaciones sin dependencia de infraestructura.
- Integration tests validan wiring, persistencia y seguridad con `@SpringBootTest` y H2 en memoria.
- Las pruebas son deterministas, aisladas y ejecutables en CI.
