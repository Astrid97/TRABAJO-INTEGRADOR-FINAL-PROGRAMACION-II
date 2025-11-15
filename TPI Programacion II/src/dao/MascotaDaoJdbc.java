/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import entities.Mascota;
import java.sql.*;
import java.util.*;

/**
 *
 * @author emlav
 */
public class MascotaDaoJdbc implements GenericDao<Mascota> {

    @Override
    public Mascota crear(Mascota m) {
        try (Connection c = DatabaseConnection.getConnection()) {
            return crear(m, c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear mascota", e);
        }
    }

    @Override
    public Mascota crear(Mascota m, Connection c) {
        String sql = "INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, eliminado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            ps.setString(3, m.getRaza());
            ps.setDate(4, java.sql.Date.valueOf(m.getFechaNacimiento()));
            ps.setString(5, m.getDuenio());
            ps.setBoolean(6, m.isEliminado());
            ps.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getLong(1));
                }
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar mascota", e);
        }
    }

    @Override
    public Optional<Mascota> leer(long id) {
        String sql = "SELECT * FROM mascota WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mascota m = new Mascota();
                    m.setId(rs.getLong("id"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecie(rs.getString("especie"));
                    m.setRaza(rs.getString("raza"));
                    m.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    m.setDuenio(rs.getString("duenio"));
                    m.setEliminado(rs.getBoolean("eliminado"));
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer mascota", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mascota> leerTodos() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascota";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mascota m = new Mascota();
                m.setId(rs.getLong("id"));
                m.setNombre(rs.getString("nombre"));
                m.setEspecie(rs.getString("especie"));
                m.setRaza(rs.getString("raza"));
                m.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                m.setDuenio(rs.getString("duenio"));
                m.setEliminado(rs.getBoolean("eliminado"));
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar mascotas", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Mascota m) {
        String sql = "UPDATE mascota SET nombre=?, especie=?, raza=?, fecha_nacimiento=?, duenio=?, eliminado=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            ps.setString(3, m.getRaza());
            ps.setDate(4, java.sql.Date.valueOf(m.getFechaNacimiento()));
            ps.setString(5, m.getDuenio());
            ps.setBoolean(6, m.isEliminado());
            ps.setLong(7, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar mascota", e);
        }
    }

    @Override
    public void eliminar(long id) {
        String sql = "DELETE FROM mascota WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mascota", e);
        }
    }

    // Métodos con Connection
    @Override
    public Optional<Mascota> leer(long id, Connection c) {
        String sql = "SELECT * FROM mascota WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mascota m = new Mascota();
                    m.setId(rs.getLong("id"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecie(rs.getString("especie"));
                    m.setRaza(rs.getString("raza"));
                    m.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    m.setDuenio(rs.getString("duenio"));
                    m.setEliminado(rs.getBoolean("eliminado"));
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer mascota (transaccional)", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mascota> leerTodos(Connection c) {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascota";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mascota m = new Mascota();
                m.setId(rs.getLong("id"));
                m.setNombre(rs.getString("nombre"));
                m.setEspecie(rs.getString("especie"));
                m.setRaza(rs.getString("raza"));
                m.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                m.setDuenio(rs.getString("duenio"));
                m.setEliminado(rs.getBoolean("eliminado"));
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar mascotas (transaccional)", e);
        }
        return lista;
    }

    @Override
    public void eliminar(long id, Connection c) {
        String sql = "DELETE FROM mascota WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mascota (transaccional)", e);
        }
    }

    @Override
    public void actualizar(Mascota m, Connection c) {
        String sql = "UPDATE mascota SET nombre=?, especie=?, raza=?, fecha_nacimiento=?, duenio=?, eliminado=? WHERE id=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            ps.setString(3, m.getRaza());
            ps.setDate(4, java.sql.Date.valueOf(m.getFechaNacimiento()));
            ps.setString(5, m.getDuenio());
            ps.setBoolean(6, m.isEliminado());
            ps.setLong(7, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar mascota (transaccional)", e);
        }
    }

}
