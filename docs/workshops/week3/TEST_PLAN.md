# Plan de Pruebas - Feature de Autenticación

# 1. Introducción
Este plan define cómo se verifican las capacidades del feature de autenticación JWT para el taller de Semana 3. Se basa en los escenarios definidos en [docs/workshops/week2/TESTING_STRATEGY.md](../week2/TESTING_STRATEGY.md) y en los requerimientos derivados en [docs/workshops/week3/REQUIREMENTS_DERIVED_FROM_STRATEGY.md](REQUIREMENTS_DERIVED_FROM_STRATEGY.md). El objetivo es garantizar pruebas repetibles, alineadas con CI, para registro, login, manejo de tokens y acceso a endpoints protegidos.

# 2. Alcance de las Pruebas
| Alcance | Descripción |
| --- | --- |
| Registro de usuarios | Validación de email, username y password |
| Login | Autenticación usando username o email |
| Generación de JWT | Creación de token tras autenticación exitosa |
| Validación de token | Verificación de expiración y formato |
| Acceso a endpoints protegidos | Validación de autorización basada en token |

| Fuera de alcance |
| --- |
| Autorización por roles |
| Refresh tokens |
| MFA |
| OAuth |
| Rate limiting |
| Auditoría de seguridad |

# 3. Estrategia de Pruebas Multinivel
| Nivel de prueba | Objetivo | Herramientas | Tipo |
| --- | --- | --- | --- |
| Component Tests | Validar lógica interna del dominio | JUnit + Mockito | White Box |
| Integration Tests | Validar comportamiento del sistema completo | SpringBootTest | Black Box |

Las pruebas de caja negra son el enfoque principal para validar el comportamiento observable del sistema. Estas pruebas verifican endpoints REST, respuestas HTTP, acceso a recursos protegidos y el flujo completo de autenticación sin depender de la implementación interna. Este enfoque es el que se expone en la defensa del proyecto.

# 4. Técnicas de Prueba
| Técnica | Aplicación |
| --- | --- |
| White Box Testing | Validación de reglas internas del dominio |
| Black Box Testing | Validación de endpoints y comportamiento observable |

Las pruebas de caja negra se aplican principalmente en los Integration Tests, donde se valida el comportamiento del sistema desde la perspectiva del cliente.

# 5. Aplicación de los 7 Principios de Testing
| Principio | Aplicación en el proyecto |
| --- | --- |
| Testing shows presence of defects | pruebas negativas de validación |
| Exhaustive testing is impossible | selección de escenarios representativos |
| Early testing | ejecución temprana en CI |
| Defect clustering | foco en login y validaciones |
| Pesticide paradox | ejecución continua en CI |
| Testing depends on context | uso de H2 en pruebas |
| Absence-of-errors fallacy | passing tests no implica seguridad total |

# 6. Matriz de Trazabilidad
| Requerimiento | Criterio de aceptación | Nivel de prueba | Tipo de prueba | Método de verificación |
| --- | --- | --- | --- | --- |
| [REQ-AUTH-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-001) | [AC-AUTH-REG-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-001) | Component | White Box | Prueba de componente del caso de uso de registro |
| [REQ-AUTH-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-001) | [AC-AUTH-REG-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-001) | Integration | Black Box | Flujo del endpoint de registro con SpringBootTest |
| [REQ-AUTH-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-002) | [AC-AUTH-REG-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-002) | Component | White Box | Validación de email duplicado en caso de uso |
| [REQ-AUTH-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-002) | [AC-AUTH-REG-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-002) | Integration | Black Box | Endpoint de registro con email existente |
| [REQ-AUTH-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-003) | [AC-AUTH-REG-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-003) | Component | White Box | Validación de username duplicado en caso de uso |
| [REQ-AUTH-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-003) | [AC-AUTH-REG-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-003) | Integration | Black Box | Endpoint de registro con username existente |
| [REQ-AUTH-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-004) | [AC-AUTH-REG-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-004) | Component | White Box | Regla de validación de formato de email |
| [REQ-AUTH-005](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-005) | [AC-AUTH-REG-005](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-005) | Component | White Box | Regla de robustez de password |
| [REQ-AUTH-006](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-006) | [AC-AUTH-LOGIN-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-001) | Component | White Box | Caso de uso de login con username |
| [REQ-AUTH-006](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-006) | [AC-AUTH-LOGIN-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-001) | Integration | Black Box | Endpoint de login con username |
| [REQ-AUTH-007](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-007) | [AC-AUTH-LOGIN-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-002) | Component | White Box | Caso de uso de login con email |
| [REQ-AUTH-007](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-007) | [AC-AUTH-LOGIN-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-002) | Integration | Black Box | Endpoint de login con email |
| [REQ-AUTH-008](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-008) | [AC-AUTH-LOGIN-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-003) | Component | White Box | Respuesta de password inválido en caso de uso |
| [REQ-AUTH-009](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-009) | [AC-AUTH-LOGIN-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-004) | Component | White Box | Rechazo de usuario inactivo |
| [REQ-AUTH-010](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-010) | [AC-AUTH-TOKEN-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-001) | Component | White Box | Generación y validación de token |
| [REQ-AUTH-011](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-011) | [AC-AUTH-TOKEN-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-002) | Component | White Box | Validación de expiración de token |
| [REQ-AUTH-012](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-012) | [AC-AUTH-TOKEN-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-003) | Component | White Box | Validación de token malformado |
| [REQ-AUTH-013](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-013) | [AC-AUTH-PROTECT-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-protect-001) | Integration | Black Box | Endpoint protegido sin token |
| [REQ-AUTH-014](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-014) | [AC-AUTH-PROTECT-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-protect-002) | Integration | Black Box | Endpoint protegido con token válido |

# 7. Casos de Prueba
| ID | Requerimiento | Criterio | Nivel | Tipo | Precondición | Resultado esperado |
| --- | --- | --- | --- | --- | --- | --- |
| TC-AUTH-REG-001 | [REQ-AUTH-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-001) | [AC-AUTH-REG-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-001) | Integration | Black Box | Usuario no existe en H2 | Registro exitoso |
| TC-AUTH-REG-002 | [REQ-AUTH-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-002) | [AC-AUTH-REG-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-002) | Integration | Black Box | Email ya registrado | Registro rechazado por email duplicado |
| TC-AUTH-REG-003 | [REQ-AUTH-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-003) | [AC-AUTH-REG-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-003) | Integration | Black Box | Username ya registrado | Registro rechazado por username duplicado |
| TC-AUTH-REG-004 | [REQ-AUTH-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-004) | [AC-AUTH-REG-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-004) | Component | White Box | Ninguna | Registro rechazado por email inválido |
| TC-AUTH-REG-005 | [REQ-AUTH-005](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-005) | [AC-AUTH-REG-005](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-reg-005) | Component | White Box | Ninguna | Registro rechazado por password débil |
| TC-AUTH-LOGIN-001 | [REQ-AUTH-006](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-006) | [AC-AUTH-LOGIN-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-001) | Integration | Black Box | Usuario activo con username | Login exitoso y token JWT devuelto |
| TC-AUTH-LOGIN-002 | [REQ-AUTH-007](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-007) | [AC-AUTH-LOGIN-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-002) | Integration | Black Box | Usuario activo con email | Login exitoso y token JWT devuelto |
| TC-AUTH-LOGIN-003 | [REQ-AUTH-008](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-008) | [AC-AUTH-LOGIN-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-003) | Component | White Box | Usuario activo existente | Login rechazado por password inválido |
| TC-AUTH-LOGIN-004 | [REQ-AUTH-009](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-009) | [AC-AUTH-LOGIN-004](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-login-004) | Component | White Box | Usuario inactivo existente | Login rechazado por usuario inactivo |
| TC-AUTH-TOKEN-001 | [REQ-AUTH-010](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-010) | [AC-AUTH-TOKEN-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-001) | Component | White Box | Autenticación exitosa | Token generado y validado |
| TC-AUTH-TOKEN-002 | [REQ-AUTH-011](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-011) | [AC-AUTH-TOKEN-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-002) | Component | White Box | Token expirado | Validación falla por expiración |
| TC-AUTH-TOKEN-003 | [REQ-AUTH-012](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-012) | [AC-AUTH-TOKEN-003](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-token-003) | Component | White Box | Token malformado | Validación falla por formato |
| TC-AUTH-PROTECT-001 | [REQ-AUTH-013](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-013) | [AC-AUTH-PROTECT-001](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-protect-001) | Integration | Black Box | Endpoint protegido disponible | Acceso denegado sin token |
| TC-AUTH-PROTECT-002 | [REQ-AUTH-014](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#req-auth-014) | [AC-AUTH-PROTECT-002](./REQUIREMENTS_DERIVED_FROM_STRATEGY.md#ac-auth-protect-002) | Integration | Black Box | Token válido obtenido por login | Acceso permitido con token válido |

# 8. Integración con CI/CD
La canalización de CI descrita en [CI workflow](../../../.github/workflows/ci.yml) ejecuta pruebas unitarias, de componente e integración, y valida seguridad y empaquetado. Esto asegura que el feature se verifique en cada cambio.

| Job del Pipeline | Tipo de Validación | Objetivo |
| --- | --- | --- |
| unit-tests | Unit Testing | validar lógica básica |
| component-tests | Component Testing | validar reglas de negocio |
| integration-tests | Integration Testing | validar comportamiento observable |
| docker-build | Build | construir imagen del servicio |
| trivy-scan | Security Scan | detectar vulnerabilidades |
| sbom | Dependency Analysis | generar inventario de dependencias |

# 9. Evidencia de Ejecución
## 9.1 Pipeline CI exitoso
Captura esperada: [docs/workshops/week3/evidence/pipeline-success.png](evidence/pipeline-success.png)
Descripción: Captura del pipeline en estado GREEN donde todos los jobs finalizan correctamente.

## 9.2 Ejecución de pruebas automatizadas
Captura esperada: [docs/workshops/week3/evidence/tests-execution.png](evidence/tests-execution.png)
Descripción: Captura del job donde se observa la ejecución de unit tests, component tests e integration tests.

## 9.3 Escaneo de seguridad de imagen Docker
Captura esperada: [docs/workshops/week3/evidence/trivy-report.png](evidence/trivy-report.png)
Descripción: Resultado del escaneo de vulnerabilidades generado por Trivy.

## 9.4 Generación de SBOM
Captura esperada: [docs/workshops/week3/evidence/sbom-artifact.png](evidence/sbom-artifact.png)
Descripción: Artifact generado con CycloneDX mostrando la lista de dependencias del proyecto.
