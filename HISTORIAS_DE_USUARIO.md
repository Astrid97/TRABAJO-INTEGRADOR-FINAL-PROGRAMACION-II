Historias de Usuario – Sistema de Gestión de Mascotas y Microchips

Especificaciones funcionales del sistema CRUD de mascotas y microchips, operado mediante menú de consola.

Tabla de Contenidos

Épica 1: Gestión de Mascotas

Épica 2: Gestión de Microchips

Reglas de Negocio

Modelo de Datos

Épica 1: Gestión de Mascotas
HU-001: Crear Mascota

Como usuario del sistema de veterinaria
Quiero crear una nueva mascota con sus datos básicos
Para almacenarla en la base de datos y poder gestionarla luego

Criterios de Aceptación
Escenario: Crear mascota con datos válidos
  Dado que el usuario selecciona "1. Crear Mascota" en el menú
  Cuando ingresa nombre, especie, raza, fecha de nacimiento y nombre del dueño
  Entonces el sistema crea un registro de mascota en la base de datos
  Y genera automáticamente un ID para la mascota
  Y marca el campo "eliminado" en false

Escenario: Intento de crear mascota con campos obligatorios vacíos
  Dado que el usuario selecciona "1. Crear Mascota"
  Cuando deja vacío alguno de los campos obligatorios (por ejemplo, nombre o especie)
  Entonces el sistema muestra un mensaje indicando que los campos son obligatorios
  Y no crea el registro de mascota

HU-002: Listar Mascotas

Como usuario del sistema de veterinaria
Quiero listar todas las mascotas registradas
Para tener una visión general de los registros existentes

Criterios de Aceptación
Escenario: Listar mascotas cuando existen registros
  Dado que hay una o más mascotas registradas en la base de datos
  Cuando el usuario selecciona "2. Listar Mascotas"
  Entonces el sistema muestra en consola el ID y los datos principales de cada mascota

Escenario: Listar mascotas cuando no existen registros
  Dado que no hay mascotas registradas
  Cuando el usuario selecciona "2. Listar Mascotas"
  Entonces el sistema muestra un mensaje indicando que no hay mascotas para listar

HU-003: Buscar Mascota por ID

Como usuario del sistema de veterinaria
Quiero buscar una mascota por su ID
Para consultar en detalle sus datos

Criterios de Aceptación
Escenario: Buscar mascota con ID válido
  Dado que existe una mascota con un ID determinado
  Cuando el usuario selecciona "3. Buscar Mascota por ID"
  Y ingresa un ID válido
  Entonces el sistema muestra todos los datos de la mascota correspondiente

Escenario: Buscar mascota con ID inexistente
  Dado que no existe ninguna mascota con el ID ingresado
  Cuando el usuario intenta buscar por ese ID
  Entonces el sistema muestra un mensaje indicando que la mascota no fue encontrada

HU-004: Actualizar Mascota

Como usuario del sistema de veterinaria
Quiero actualizar los datos de una mascota existente
Para mantener la información correcta y al día

Criterios de Aceptación
Escenario: Actualizar mascota con datos válidos
  Dado que existe una mascota con un ID determinado
  Cuando el usuario selecciona "4. Actualizar Mascota"
  Y el sistema solicita los nuevos datos
  Y el usuario ingresa datos válidos
  Entonces el sistema actualiza el registro de la mascota en la base de datos

Escenario: Intento de actualizar mascota inexistente
  Dado que no existe una mascota con el ID ingresado
  Cuando el usuario selecciona "4. Actualizar Mascota"
  Y proporciona un ID inexistente
  Entonces el sistema informa que la mascota no fue encontrada
  Y no realiza ninguna actualización

HU-005: Eliminar Mascota

Como usuario del sistema de veterinaria
Quiero eliminar una mascota del sistema de forma lógica
Para que deje de considerarse activa sin borrar su registro definitivamente

Criterios de Aceptación
Escenario: Eliminar mascota existente
  Dado que existe una mascota con un ID determinado
  Cuando el usuario selecciona "5. Eliminar Mascota"
  Y confirma la operación
  Entonces el sistema actualiza el campo "eliminado" de la mascota a true

Escenario: Intento de eliminar mascota inexistente
  Dado que no existe una mascota con el ID ingresado
  Cuando el usuario selecciona "5. Eliminar Mascota"
  Y proporciona un ID inválido o inexistente
  Entonces el sistema informa que la mascota no fue encontrada
  Y no realiza ninguna modificación

Épica 2: Gestión de Microchips
HU-006: Agregar Microchip a Mascota

Como usuario del sistema de veterinaria
Quiero agregar un microchip a una mascota existente
Para poder identificarla de forma única mediante su dispositivo implantado

Criterios de Aceptación
Escenario: Agregar microchip a mascota sin microchip
  Dado que existe una mascota sin microchip asociado
  Cuando el usuario selecciona "6. Agregar Microchip a Mascota"
  Y el usuario ingresa el ID de la mascota, el número de microchip y la fecha de implantación
  Entonces el sistema crea un registro de microchip en la base de datos
  Y lo asocia a la mascota indicada

Escenario: Intento de agregar microchip a mascota que ya tiene microchip
  Dado que existe una mascota que ya tiene un microchip asociado
  Cuando el usuario selecciona "6. Agregar Microchip a Mascota"
  Y utiliza el ID de esa mascota
  Entonces el sistema muestra un mensaje indicando que la mascota ya tiene microchip
  Y no crea un nuevo registro duplicado

HU-007: Actualizar Microchip

Como usuario del sistema de veterinaria
Quiero actualizar los datos de un microchip registrado
Para corregir información o mantener los datos actualizados

Criterios de Aceptación
Escenario: Actualizar microchip existente
  Dado que existe un microchip registrado en el sistema
  Cuando el usuario selecciona "7. Actualizar Microchip"
  Y proporciona un identificador válido (por ejemplo, ID o número de microchip, según implementación)
  Y luego ingresa los nuevos datos requeridos
  Entonces el sistema actualiza el registro de microchip en la base de datos

Escenario: Intento de actualizar microchip inexistente
  Dado que no existe un microchip con el identificador ingresado
  Cuando el usuario selecciona "7. Actualizar Microchip"
  Y proporciona un identificador inválido
  Entonces el sistema muestra un mensaje indicando que el microchip no fue encontrado

HU-008: Buscar Microchip por ID

Como usuario del sistema de veterinaria
Quiero buscar un microchip por su identificador
Para consultar sus datos y la mascota asociada

Criterios de Aceptación
Escenario: Buscar microchip con ID válido
  Dado que existe un microchip con un ID determinado
  Cuando el usuario selecciona "8. Buscar Microchip por ID"
  Y proporciona un ID válido
  Entonces el sistema muestra los datos del microchip
  Y, si corresponde, los datos básicos de la mascota asociada

Escenario: Buscar microchip con ID inexistente
  Dado que no existe un microchip con el ID ingresado
  Cuando el usuario intenta buscarlo
  Entonces el sistema muestra un mensaje indicando que el microchip no fue encontrado

HU-009: Eliminar Microchip

Como usuario del sistema de veterinaria
Quiero eliminar un microchip del sistema
Para que deje de estar disponible o asociado a una mascota en las operaciones habituales

Criterios de Aceptación
Escenario: Eliminar microchip existente
  Dado que existe un microchip en la base de datos
  Cuando el usuario selecciona "9. Eliminar Microchip"
  Y proporciona un identificador válido
  Entonces el sistema elimina (o marca como eliminado, según la implementación) el microchip

Escenario: Intento de eliminar microchip inexistente
  Dado que no existe un microchip con el identificador ingresado
  Cuando el usuario selecciona "9. Eliminar Microchip"
  Entonces el sistema muestra un mensaje indicando que el microchip no fue encontrado

Reglas de Negocio

RN-001: Nombre, especie y dueño de la mascota son obligatorios.

RN-002: El ID de la mascota y del microchip se generan automáticamente por la base de datos.

RN-003: El campo eliminado se utiliza para eliminación lógica de mascotas (soft delete).

RN-004: Una mascota puede tener a lo sumo un microchip asociado (relación 1 a 1).

RN-005: No se puede asociar un microchip a una mascota inexistente.

RN-006: No se puede duplicar el microchip de una mascota que ya tiene uno asignado.

Modelo de Datos

Tabla mascota: almacena los datos básicos de la mascota

id (PK, autoincremental)

nombre, especie, raza, fecha_nacimiento, duenio

eliminado (boolean para soft delete)

Tabla microchip: almacena la información del microchip

id (PK, autoincremental)

numero (identificador único del microchip, según diseño)

fecha_implantacion

id_mascota (FK hacia mascota.id)

Relación:

Una mascota puede tener como máximo un microchip asociado.

Cada microchip está asociado a exactamente una mascota.