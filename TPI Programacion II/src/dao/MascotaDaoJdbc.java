/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import entities.Mascota;
import entities.Microchip;
import java.sql.*;
import java.util.*;

/**
 * Implementación JDBC del DAO para la entidad Mascota.
 *
 * Esta clase se encarga de realizar todas las operaciones CRUD contra la base
 * de datos utilizando JDBC.
 *
 * Incluye dos versiones de cada método: - una que maneja su propia conexión -
 * una transaccional que recibe un Connection externo
 *
 * También implementa borrado lógico mediante el campo "eliminado".
 */
public class MascotaDaoJdbc implements GenericDao<Mascota> {

    /**
     * Necesitamos microchip para relacionarlo.
     */
    private final MicrochipDaoJdbc microchipDao = new MicrochipDaoJdbc();

    /**
     * Crear una mascota usando una conexión propia.
     */
    @Override
    public Mascota crear(Mascota m) {
        try (Connection c = DatabaseConnection.getConnection()) {
            return crear(m, c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear mascota", e);
        }
    }

    /**
     * Buscar una mascota usando una conexión propia.
     */
    @Override
    public Optional<Mascota> leer(long id) {
        String sql = "SELECT m.*, m.microchip_id FROM mascota m WHERE m.id = ? AND eliminado = FALSE";
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
                    Long microchipId = rs.getLong("microchip_id");
                    if (microchipId != 0) {
                        microchipDao.leer(microchipId).ifPresent(m::setMicrochip);
                    }
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer mascota", e);
        }
        return Optional.empty();
    }

    /**
     * Listar las mascotas usando una conexión propia.
     */
    @Override
    public List<Mascota> leerTodos() {
        List<Mascota> lista = new ArrayList<>();

        String sql
                = "SELECT m.id, m.nombre, m.especie, m.raza, m.fecha_nacimiento, "
                + "       m.duenio, m.eliminado, m.microchip_id, "
                + "       mc.id              AS mc_id, "
                + "       mc.eliminado       AS mc_eliminado, "
                + "       mc.codigo          AS mc_codigo, "
                + "       mc.fecha_implantacion AS mc_fecha_implantacion, "
                + "       mc.veterinaria     AS mc_veterinaria, "
                + "       mc.observaciones   AS mc_observaciones "
                + "FROM mascota m "
                + "LEFT JOIN microchip mc ON mc.id = m.microchip_id "
                + "WHERE m.eliminado = FALSE";

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

                // Microchip (puede ser null)
                long mcId = rs.getLong("mc_id");
                if (!rs.wasNull()) {
                    Microchip mc = new Microchip();
                    mc.setId(mcId);
                    mc.setEliminado(rs.getBoolean("mc_eliminado"));
                    mc.setCodigo(rs.getString("mc_codigo"));
                    mc.setFechaImplantacion(rs.getDate("mc_fecha_implantacion").toLocalDate());

                    mc.setVeterinaria(rs.getString("mc_veterinaria"));
                    mc.setObservaciones(rs.getString("mc_observaciones"));

                    m.setMicrochip(mc);
                }

                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar mascotas", e);
        }

        return lista;
    }

    /**
     * Actualizar una mascota usando una conexión propia.
     */
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

    /**
     * Eliminación lógica de una mascota usando una conexión propia.
     */
    @Override
    public void eliminar(long id) {
        try (Connection c = DatabaseConnection.getConnection()) {
            eliminar(id, c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mascota", e);
        }
    }

    /**
     * Recuperación de una mascota usando una conexión propia.
     */
    public void recuperar(long id) {
        String sql = "UPDATE mascota SET eliminado = FALSE WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar mascota", e);
        }
    }

    // Métodos con conexión
    /**
     * Crear una mascota usando una conexión externa.
     */
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

    /**
     * Leer una mascota usando una conexión externa.
     */
    @Override
    public Optional<Mascota> leer(long id, Connection c) {
        String sql = "SELECT * FROM mascota WHERE id = ? AND eliminado = FALSE";
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
                    Long microchipId = rs.getLong("microchip_id");
                    if (microchipId != 0) {
                        microchipDao.leer(microchipId).ifPresent(m::setMicrochip);
                    }
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer mascota (transaccional)", e);
        }
        return Optional.empty();
    }

    /**
     * Listar mascotas usando una conexión externa.
     */
    @Override
    public List<Mascota> leerTodos(Connection c) {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascota WHERE eliminado = FALSE";
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
                Long microchipId = rs.getLong("microchip_id");
                if (microchipId != 0) {
                    microchipDao.leer(microchipId).ifPresent(m::setMicrochip);
                }
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar mascotas (transaccional)", e);
        }
        return lista;
    }

    /**
     * Eliminar logicamente una mascota usando una conexión externa.
     */
    @Override
    public void eliminar(long id, Connection c) {
        // Primero obtenemos la mascota para ver si tiene microchip
        Optional<Mascota> opt = leer(id, c);
        if (opt.isPresent()) {
            Mascota m = opt.get();
            // Si tiene microchip, eliminarlo también
            if (m.getMicrochip() != null) {
                microchipDao.eliminar(m.getMicrochip().getId(), c);
            }
        }

        // Ahora eliminamos la mascota
        String sql = "UPDATE mascota SET eliminado = TRUE WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mascota (transaccional)", e);
        }
    }

    /**
     * Actualizar una mascota usando una conexión externa.
     */
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

    /**
     * Recuperar una mascota usando una conexión externa.
     */
    public void recuperar(long id, Connection c) {
        String sql = "UPDATE mascota SET eliminado = FALSE WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar mascota (transaccional)", e);
        }
    }
}
