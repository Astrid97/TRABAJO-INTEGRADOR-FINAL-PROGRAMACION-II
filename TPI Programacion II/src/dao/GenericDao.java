/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

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
