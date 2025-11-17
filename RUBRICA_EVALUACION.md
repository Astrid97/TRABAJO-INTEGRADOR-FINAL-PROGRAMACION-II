# Rúbrica de Evaluación - Trabajo Práctico Integrador
## Programación 2

---

**Proyecto**: Sistema de Gestión de Personas y Domicilios
**Tipo de Evaluación**: Trabajo Práctico Integrador (TPI)
**Puntaje Total**: 100 puntos
**Puntaje Mínimo para Aprobar**: 60 puntos (6/10)

---

## Criterios de Evaluación

### 1. ARQUITECTURA Y DISEÑO (30 puntos)

#### 1.1 Separación en Capas (10 puntos)

| Criterio | Excelente (9-10) | Muy Bueno (7-8) | Bueno (5-6) | Insuficiente (0-4) |
|----------|------------------|-----------------|-------------|-------------------|
| **Organización en capas** | Arquitectura de 4 capas perfectamente separadas (Main, Service, DAO, Models). Sin dependencias cruzadas. | 4 capas implementadas con alguna dependencia menor no crítica. | 3-4 capas con algunas violaciones de separación. | Menos de 3 capas o mezcla significativa de responsabilidades. |
| **Responsabilidades definidas** | Cada capa tiene responsabilidades claras y bien delimitadas. | Responsabilidades mayormente claras con alguna ambigüedad menor. | Responsabilidades parcialmente definidas. | Responsabilidades confusas o mezcladas. |

**Puntos obtenidos: _____ / 10**

#### 1.2 Patrones de Diseño (10 puntos)

| Criterio | Excelente (9-10) | Muy Bueno (7-8) | Bueno (5-6) | Insuficiente (0-4) |
|----------|------------------|-----------------|-------------|-------------------|
| **DAO Pattern** | DAO correctamente implementado con separación de SQL. Queries como constantes. | DAO implementado, SQL mayormente separado. | DAO parcialmente implementado o SQL mezclado. | DAO ausente o incorrectamente implementado. |
| **Service Layer** | Lógica de negocio completamente en Service. Validaciones antes de persistir. | Lógica mayormente en Service. Alguna validación en otras capas. | Lógica parcialmente en Service. Validaciones inconsistentes. | Lógica de negocio mezclada en múltiples capas. |
| **Otros patrones** | Factory, Dependency Injection y otros patrones correctos. | Algunos patrones implementados correctamente. | Patrones básicos o incorrectos. | Sin patrones de diseño reconocibles. |

**Puntos obtenidos: _____ / 10**

#### 1.3 Programación Orientada a Objetos (10 puntos)

| Criterio | Excelente (9-10) | Muy Bueno (7-8) | Bueno (5-6) | Insuficiente (0-4) |
|----------|------------------|-----------------|-------------|-------------------|
| **Herencia y Abstracción** | Uso correcto de clases abstractas e interfaces genéricas. | Herencia/interfaces implementadas con errores menores. | Herencia básica sin aprovechar abstracción. | Sin herencia o implementación incorrecta. |
| **Encapsulamiento** | Atributos privados, getters/setters apropiados. Sin acceso directo. | Atributos privados, acceso controlado con excepciones menores. | Encapsulamiento parcial. | Atributos públicos o sin control de acceso. |
| **Polimorfismo** | Uso efectivo de interfaces y sobrescritura (equals, hashCode, toString). | Polimorfismo presente con implementación básica. | Sobrescritura mínima de métodos. | Sin polimorfismo o implementación incorrecta. |

**Puntos obtenidos: _____ / 10**

---

### 2. PERSISTENCIA DE DATOS CON JDBC (25 puntos)

#### 2.1 Conexión y Operaciones CRUD (10 puntos)

| Criterio | Excelente (9-10) | Muy Bueno (7-8) | Bueno (5-6) | Insuficiente (0-4) |
|----------|------------------|-----------------|-------------|-------------------|
| **Conexión a BD** | Conexión correcta con configuración centralizada y validación. | Conexión funcional con configuración básica. | Conexión funciona pero sin validación. | Conexión no funciona o hard-coded. |
| **Operaciones CRUD** | Todas las operaciones (Create, Read, Update, Delete) funcionan correctamente. | 3-4 operaciones funcionan correctamente. | 2 operaciones funcionan. | 1 o ninguna operación funciona. |
| **Manejo de IDs autogenerados** | IDs autogenerados recuperados y asignados correctamente (RETURN_GENERATED_KEYS). | IDs recuperados con algún error menor. | IDs no siempre recuperados correctamente. | IDs no manejados o asignados manualmente. |

**Puntos obtenidos: _____ / 10**

#### 2.2 Seguridad y Consultas (8 puntos)

| Criterio | Excelente (7-8) | Bueno (5-6) | Regular (3-4) | Insuficiente (0-2) |
|----------|-----------------|-------------|---------------|-------------------|
| **PreparedStatements** | 100% de consultas usan PreparedStatements. | >80% de consultas usan PreparedStatements. | >50% de consultas usan PreparedStatements. | <50% o queries concatenadas (SQL injection). |
| **Consultas con JOIN** | JOIN implementado correctamente (LEFT/INNER). Manejo correcto de NULL. | JOIN funciona con manejo básico de NULL. | JOIN implementado pero con errores. | Sin JOIN o implementación incorrecta. |

**Puntos obtenidos: _____ / 8**

#### 2.3 Transacciones (7 puntos)

| Criterio | Excelente (6-7) | Bueno (4-5) | Regular (2-3) | Insuficiente (0-1) |
|----------|-----------------|-------------|---------------|-------------------|
| **Gestión de transacciones** | Soporte para transacciones con commit/rollback. Métodos transaccionales (insertTx). | Transacciones básicas implementadas. | Intento de transacciones con errores. | Sin soporte transaccional o autocommit siempre activo. |
| **Atomicidad** | Operaciones compuestas son atómicas (rollback en error). | Intento de atomicidad con implementación parcial. | Sin atomicidad en operaciones compuestas. | N/A |

**Puntos obtenidos: _____ / 7**

---

### 3. MANEJO DE RECURSOS Y EXCEPCIONES (20 puntos)

#### 3.1 Gestión de Recursos JDBC (12 puntos)

| Criterio | Excelente (11-12) | Muy Bueno (9-10) | Bueno (6-8) | Insuficiente (0-5) |
|----------|-------------------|------------------|-------------|-------------------|
| **Try-with-resources** | 100% de recursos JDBC usan try-with-resources. | >80% de recursos usan try-with-resources. | >50% de recursos usan try-with-resources. | <50% o cierre manual inconsistente. |
| **Cierre de conexiones** | Todas las conexiones se cierran correctamente. Sin resource leaks. | Conexiones mayormente cerradas. | Algunas conexiones no cerradas. | Resource leaks evidentes. |
| **AutoCloseable** | Implementación de AutoCloseable en clases propias (ej: TransactionManager). | Intento de AutoCloseable. | Sin implementación de AutoCloseable. | N/A |

**Puntos obtenidos: _____ / 12**

#### 3.2 Manejo de Excepciones (8 puntos)

| Criterio | Excelente (7-8) | Bueno (5-6) | Regular (3-4) | Insuficiente (0-2) |
|----------|-----------------|-------------|---------------|-------------------|
| **Try-catch apropiado** | Excepciones capturadas en el nivel correcto. Propagación controlada. | Try-catch presente con propagación básica. | Try-catch con catch genérico o silencioso. | Sin manejo o printStackTrace sin control. |
| **Mensajes al usuario** | Mensajes informativos y amigables. No se exponen detalles técnicos. | Mensajes básicos pero funcionales. | Mensajes técnicos o confusos. | Sin mensajes o aplicación crashea. |

**Puntos obtenidos: _____ / 8**

---

### 4. VALIDACIÓN E INTEGRIDAD DE DATOS (15 puntos)

#### 4.1 Validaciones de Entrada (8 puntos)

| Criterio | Excelente (7-8) | Bueno (5-6) | Regular (3-4) | Insuficiente (0-2) |
|----------|-----------------|-------------|---------------|-------------------|
| **Campos obligatorios** | Validación de campos requeridos en Service layer. Mensajes claros. | Validación presente con mensajes básicos. | Validación parcial o solo en UI. | Sin validación o solo en BD. |
| **Sanitización de entrada** | Input trimming consistente. Prevención de espacios no deseados. | Trimming presente pero inconsistente. | Trimming mínimo. | Sin sanitización. |
| **Validación de IDs** | Validación de IDs positivos en todas las operaciones. | Validación de IDs en operaciones principales. | Validación inconsistente. | Sin validación de IDs. |

**Puntos obtenidos: _____ / 8**

#### 4.2 Integridad Referencial (7 puntos)

| Criterio | Excelente (6-7) | Bueno (4-5) | Regular (2-3) | Insuficiente (0-1) |
|----------|-----------------|-------------|---------------|-------------------|
| **Constraints en BD** | Foreign Keys, UNIQUE constraints correctamente definidos. | Constraints básicos implementados. | Algunos constraints faltantes. | Sin constraints o implementación incorrecta. |
| **Validación de unicidad** | Validación de unicidad en BD Y aplicación (ej: DNI único). | Validación solo en BD o solo en aplicación. | Validación parcial. | Sin validación de unicidad. |
| **Prevención de huérfanos** | Implementación de eliminación segura (actualizar FK antes de eliminar). | Intento de prevención pero con errores. | Sin prevención de referencias huérfanas. | N/A |

**Puntos obtenidos: _____ / 7**

---

### 5. FUNCIONALIDAD COMPLETA (10 puntos)

| Criterio | Excelente (9-10) | Muy Bueno (7-8) | Bueno (5-6) | Insuficiente (0-4) |
|----------|------------------|-----------------|-------------|-------------------|
| **Operaciones CRUD** | Todas las operaciones funcionan sin errores. | 3-4 operaciones funcionan correctamente. | 2 operaciones funcionan. | 1 o ninguna operación funciona. |
| **Búsquedas y filtros** | Búsqueda flexible con LIKE pattern matching. Filtros múltiples. | Búsqueda básica funcional. | Búsqueda con errores. | Sin funcionalidad de búsqueda. |
| **Interfaz de usuario** | Menú claro, navegación intuitiva, manejo de errores robusto. | Menú funcional con algunas ambigüedades. | Menú básico con errores de UX. | Menú confuso o no funcional. |
| **Soft Delete** | Eliminación lógica implementada correctamente en todas las operaciones. | Soft delete implementado con inconsistencias menores. | Soft delete parcial. | Sin soft delete o implementación incorrecta. |

**Puntos obtenidos: _____ / 10**

---

## BONIFICACIONES (máximo +10 puntos adicionales)

### Extras que Suman

| Característica | Puntos | Descripción |
|----------------|--------|-------------|
| **Documentación Javadoc completa** | +3 | Javadoc profesional en todas las clases y métodos principales con explicación del "por qué". |
| **README profesional** | +2 | README completo con instalación, uso, arquitectura y troubleshooting. |
| **Historias de Usuario** | +2 | Especificaciones funcionales con criterios de aceptación y reglas de negocio numeradas. |
| **Implementaciones avanzadas** | +2 | Coordinación transaccional, eliminación segura, validación multi-nivel. |
| **Pruebas manuales documentadas** | +1 | Documento con casos de prueba ejecutados y resultados. |

**Puntos de bonificación: _____ / 10**

---

## PENALIZACIONES

| Problema | Penalización |
|----------|--------------|
| **SQL Injection posible** | -10 puntos |
| **Resource leaks evidentes** | -5 puntos |
| **Código no compila** | -15 puntos |
| **No se puede ejecutar** | -10 puntos |
| **Datos hardcodeados sensibles** | -5 puntos |
| **Violaciones graves de encapsulamiento** | -5 puntos |
| **Uso de atajos peligrosos (catch vacío, etc.)** | -3 puntos c/u |

**Penalizaciones aplicadas: _____ puntos**

---

## RESUMEN DE EVALUACIÓN

| Categoría | Puntos Máximos | Puntos Obtenidos |
|-----------|----------------|------------------|
| 1. Arquitectura y Diseño | 30 | _____ |
| 2. Persistencia de Datos con JDBC | 25 | _____ |
| 3. Manejo de Recursos y Excepciones | 20 | _____ |
| 4. Validación e Integridad de Datos | 15 | _____ |
| 5. Funcionalidad Completa | 10 | _____ |
| **SUBTOTAL** | **100** | **_____** |
| Bonificaciones | +10 | _____ |
| Penalizaciones | - | _____ |
| **PUNTAJE FINAL** | **110** | **_____** |

---

## ESCALA DE CALIFICACIÓN

| Puntaje | Calificación | Resultado |
|---------|--------------|-----------|
| 90-100+ | 10 (Excelente) | ✅ Aprobado con Distinción |
| 80-89 | 8-9 (Muy Bueno) | ✅ Aprobado |
| 70-79 | 7 (Bueno) | ✅ Aprobado |
| 60-69 | 6 (Aprobado) | ✅ Aprobado |
| 40-59 | 4-5 (Insuficiente) | ❌ Desaprobado - Recuperable |
| 0-39 | 0-3 (Muy Insuficiente) | ❌ Desaprobado |

---

## OBSERVACIONES Y COMENTARIOS DEL EVALUADOR

### Fortalezas del Proyecto

1. _____________________________________________________________________

2. _____________________________________________________________________

3. _____________________________________________________________________

### Áreas de Mejora

1. _____________________________________________________________________

2. _____________________________________________________________________

3. _____________________________________________________________________

### Recomendaciones

_________________________________________________________________________

_________________________________________________________________________

_________________________________________________________________________

---

## VERIFICACIÓN DE CRITERIOS MÍNIMOS

Antes de evaluar con la rúbrica, verificar que el proyecto cumple con los requisitos mínimos:

- [ ] El proyecto compila sin errores
- [ ] La base de datos se conecta correctamente
- [ ] Al menos 3 operaciones CRUD funcionan
- [ ] Hay separación en al menos 3 capas (Main, DAO, Models mínimo)
- [ ] Usa PreparedStatements en al menos 70% de las consultas
- [ ] Try-with-resources en al menos 60% de operaciones JDBC

**Si NO se cumplen estos criterios mínimos, el proyecto está automáticamente DESAPROBADO (0 puntos).**

---

**Evaluador**: _______________________________
**Fecha**: _______________________________
**Firma**: _______________________________

---

## CRITERIOS DE EXCELENCIA (Puntaje 90-100+)

Un proyecto excelente debe demostrar:

1. **Arquitectura Profesional**: 4 capas perfectamente separadas, sin violaciones
2. **Seguridad Total**: 100% PreparedStatements, sin vulnerabilidades
3. **Gestión Perfecta de Recursos**: 100% try-with-resources, sin leaks
4. **Validación Robusta**: Multi-nivel (UI, Service, BD), mensajes claros
5. **Código Limpio**: Javadoc completo, convenciones consistentes, sin code smells
6. **Funcionalidad Completa**: Todas las operaciones funcionan sin errores
7. **Documentación Profesional**: README, Javadoc, historias de usuario

Este proyecto es un ejemplo de excelencia con **puntaje verificado de 9.7/10** (97 puntos base + hasta 10 de bonificación = 107/110 posibles).

---

**Nota**: Esta rúbrica está diseñada para evaluar de forma objetiva y justa el Trabajo Práctico Integrador de Programación 2, permitiendo identificar claramente fortalezas y áreas de mejora en cada aspecto del desarrollo de software orientado a objetos con persistencia de datos.
