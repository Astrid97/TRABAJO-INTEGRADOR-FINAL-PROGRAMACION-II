/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del DAO para la entidad Mascota.
 *
 * Esta clase es responsable de todas las operaciones de acceso a datos
 * relacionadas con la tabla "mascota".
 *
 * Responsabilidades:
 * - Ejecutar operaciones CRUD sobre mascotas.
 * - Manejar conexiones, sentencias y ResultSet.
 * - Mapear datos de la base a objetos de dominio.
 * - Gestionar la relación con Microchip mediante MicrochipDaoJdbc.
 * - Proveer versiones transaccionales de los métodos cuando reciben Connection.
 */

/**
 *
 * @author emlav
 */
public interface GenericDao<T> {
    T crear(T entidad);
    Optional<T> leer(long id);
    List<T> leerTodos();
    void actualizar(T entidad);
    void eliminar(long id);

    // Métodos con conexión externa
    T crear(T entidad, Connection c);
    void actualizar(T entidad, Connection c);
    void eliminar(long id, Connection c);

    Optional<T> leer(long id, Connection c);
    List<T> leerTodos(Connection c);
}
