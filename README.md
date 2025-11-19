# Sistema de Gestión de Implante de Microchips para Clínicas Veterinarias  
### Trabajo Práctico Integrador – Programación II – UTN

## 1. Integrantes
- **Astrid Añazco** – Lógica de negocio (Service), Main, Integración general  
- **Carina Aranchet** – UML, Modelado, Diseño  
- **Estefanía Avalos** – Acceso a datos (DAO), JDBC  
- **Andrea Ayala** – Configuración, Entidades, Base de Datos  

### Link al drive con documento pdf y video sobre el proyecto:

https://drive.google.com/drive/folders/1kXVxGjS1SyYSASpvG4AKyH9KZr27P7eO?usp=sharing

## 2. Descripción General del Proyecto

El proyecto consiste en un sistema de gestión para clínicas veterinarias, cuyo objetivo es administrar **mascotas** y **microchips implantados**.  

Se desarrolló utilizando **Java**, **JDBC** y **MySQL**, aplicando:
- Arquitectura en **5 capas**
- **Patrón DAO**
- **Relación unidireccional 1 → 1** (Mascota → Microchip)
- **Transacciones** con commit/rollback
- Interfaz de consola para CRUD completo
- Enfoque profesional con manejo de excepciones y validaciones de negocio
- El sistema garantiza integridad de datos y consistencia entre Mascota y Microchip.

## 3. Arquitectura del Sistema

El proyecto está organizado de acuerdo con una arquitectura multicapa, siguiendo buenas prácticas de diseño:

```
main
│── Main.java
│── AppMenu.java
│── MenuDisplay.java
└── MenuHandler.java

service
│── GenericService.java
│── MascotaService.java
└── MicrochipService.java

dao
│── GenericDao.java
│── MascotaDaoJdbc.java
└── MicrochipDaoJdbc.java

entities
│── Base.java
│── Mascota.java
└── Microchip.java

config
└── DatabaseConnection.java
└── TransactionManager.java
```

### 3.1 Capa Config
Administra la conexión a MySQL a través de JDBC.  
Centraliza la creación, obtención y liberación de `Connection`.

### 3.2 Capa Entities
Representa el modelo del dominio.  
Incluye una clase base con atributos compartidos: `id` y `eliminado`.

### 3.3 Capa DAO
Responsable del acceso directo a la base de datos. Incluye:
- CRUD completo
- Uso de `PreparedStatement`
- Conversión de `ResultSet` a objetos
- Manejo de excepciones SQL
- Reglas de acceso y consultas personalizadas

### 3.4 Capa Service
Encapsula la **lógica de negocio**, incluyendo:
- Validaciones obligatorias
- Coordinación Mascota ↔ Microchip
- Manejo de transacciones (commit / rollback)
- Reglas de negocio para actualización y eliminación

### 3.5 Capa Main
Contiene:
- Interfaz de consola
- Visualización del menú
- Lectura de entrada
- Redirección de acciones hacia las capas superiores


## 4. Relación 1 → 1 (Mascota – Microchip)

Se implementó una relación unidireccional donde **una mascota tiene un único microchip**.  

Esto asegura que:
- Un microchip solo puede pertenecer a una mascota  
- Una mascota solo puede tener un microchip  
- Al eliminar una mascota, el sistema gestiona la relación con su microchip de manera adecuada para mantener la integridad de los datos.


## 5. Modelo de Base de Datos

### 5.1 Tabla `mascota`

```sql
CREATE TABLE mascota (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    eliminado TINYINT(1) DEFAULT 0,
    nombre VARCHAR(60) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(60),
    fecha_nacimiento DATE,
    duenio VARCHAR(120) NOT NULL,
    microchip_id BIGINT UNSIGNED UNIQUE,
    FOREIGN KEY (microchip_id)
        REFERENCES microchip(id)
        ON DELETE SET NULL
);
```

### 5.2 Tabla `microchip`

```sql
CREATE TABLE microchip (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    eliminado TINYINT(1) DEFAULT 0,
    codigo VARCHAR(60) UNIQUE NOT NULL,
    fecha_implantacion DATE,
    veterinaria VARCHAR(120),
    observaciones TEXT
);
```

---

## 6. Funcionalidades del Sistema

| Funcionalidad | Mascotas | Microchips |
|---------------|----------|------------|
| Crear | ✔ | ✔ |
| Leer por ID | ✔ | ✔ |
| Listar todas | ✔ | ✔ |
| Actualizar | ✔ | ✔ |
| Eliminar | ✔ | ✔ |
| Asociar microchip a mascota | ✔ | — |
| Transacciones coordinadas | ✔ | ✔ |


## 7. Manejo de Transacciones

El sistema implementa un esquema de transacciones manuales mediante JDBC.

Se utiliza:
- **Auto-commit desactivado**
- **Commit** cuando todas las operaciones concluyen correctamente
- **Rollback** si ocurre cualquier error

Casos en los que aplica:
- Crear mascota + microchip asociado  
- Actualizar mascota + microchip  
- Cambiar asociación  
- Operaciones dependientes entre sí

Esto evita inconsistencias como:
- Mascota creada sin microchip  
- Microchip actualizado sin reflejarse en la mascota  
- Registros huérfanos


## 8. Instalación y Ejecución

### 8.1 Clonar el repositorio

```bash
git clone https://github.com/Astrid97/TRABAJO-INTEGRADOR-FINAL-PROGRAMACION-II.git
```

### 8.2 Configuración MySQL
- Usuario: `root`  
- Contraseña: (vacía o la correspondiente, según el manejo local de tu BD - verificar configuración en `src/config/DatabaseConnection.java`)  
- Ejecutar los scripts SQL otorgados en el mismo proyecto, para la creacion de la BD.

### 8.3 Importar en NetBeans
Abrir el proyecto y configurar el driver JDBC.

### 8.4 Ejecutar el programa
Desde la clase:

```
main/AppMenu.java
```

## 9. Validaciones y Reglas de Negocio (resumen)

- Las mascotas deben tener nombre, especie y dueño.
- El código del microchip es único.
- Un microchip no puede asignarse a más de una mascota.
- Las eliminaciones son lógicas (`eliminado = 1`).
- No se puede actualizar una entidad eliminada.
- Operaciones combinadas requieren transacción.
- Insertar dos microchips con el mismo código es inválido.
- Una mascota solo puede tener **un** microchip a la vez.


## 10. Estado del Proyecto

- Funcional  
- Probado  
- Validado por el equipo

## 11. Posibles Extensiones Futuras

- Interfaz gráfica (JavaFX/Swing)  
- API REST con Spring Boot  
- Sistema de turnos veterinarios  
- Reportes de actividad  
- Manejo de usuarios y roles  
- Historial clínico  
- Auditoría de operaciones  
