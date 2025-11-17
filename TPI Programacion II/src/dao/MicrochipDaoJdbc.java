package dao;

import config.DatabaseConnection;
import entities.Microchip;
import java.sql.*;
import java.util.*;

/**
 * Implementación JDBC del DAO para la entidad Microchip.
 *
 * Esta clase se encarga de realizar todas las operaciones CRUD contra la base
 * de datos utilizando JDBC.
 *
 * Incluye dos versiones de cada método: - una que maneja su propia conexión -
 * una transaccional que recibe un Connection externo
 *
 * También implementa borrado lógico mediante el campo "eliminado".
 */

/**
 *
 * @author emlav
 */

public class MicrochipDaoJdbc implements GenericDao<Microchip> {

    /**
     * Crear un microchip usando una conexión propia.
     */
    @Override
    public Microchip crear(Microchip m) {
        try (Connection c = DatabaseConnection.getConnection()) {
            return crear(m, c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear microchip", e);
        }
    }

    /**
     * Leer un microchip por ID usando una conexión propia.
     */
    @Override
    public Optional<Microchip> leer(long id) {
        String sql = "SELECT * FROM microchip WHERE id = ? AND eliminado = FALSE";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Microchip m = mapearResultSet(rs);
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer microchip: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Leer todos los microchips no eliminados.
     */
    @Override
    public List<Microchip> leerTodos() {
        List<Microchip> lista = new ArrayList<>();
        String sql = "SELECT * FROM microchip AND eliminado = FALSE";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar microchips", e);
        }
        return lista;
    }

    /**
     * Actualizar un microchip utilizando una conexión propia.
     */
    @Override
    public void actualizar(Microchip m) {
        String sql = "UPDATE microchip SET codigo=?, fecha_implantacion=?, veterinaria=?, observaciones=?, eliminado=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getCodigo());
            ps.setDate(2, java.sql.Date.valueOf(m.getFechaImplantacion()));
            ps.setString(3, m.getVeterinaria());
            ps.setString(4, m.getObservaciones());
            ps.setBoolean(5, m.isEliminado());
            ps.setLong(6, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar microchip", e);
        }
    }

    /**
     * Eliminación lógica usando conexión propia.
     */
    @Override
    public void eliminar(long id) {
        String sql = "UPDATE microchip SET eliminado = TRUE WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar microchip", e);
        }
    }

    /**
     * Recuperar mc usando conexión propia.
     */
    public void recuperar(long id) {
        String sql = "UPDATE microchip SET eliminado = FALSE WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar microchip", e);
        }
    }

    // Métodos con conexión
    /**
     * Crear un microchip utilizando una conexión externa (transacción).
     */
    @Override
    public Microchip crear(Microchip m, Connection c) {
        String sql = "INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones, eliminado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getCodigo());
            ps.setDate(2, java.sql.Date.valueOf(m.getFechaImplantacion()));
            ps.setString(3, m.getVeterinaria());
            ps.setString(4, m.getObservaciones());
            ps.setBoolean(5, m.isEliminado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getLong(1));
                }
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar microchip", e);
        }
    }

    /**
     * Actualizar un microchip usando una conexión externa.
     */
    @Override
    public void actualizar(Microchip m, Connection c) {
        String sql = "UPDATE microchip SET codigo=?, fecha_implantacion=?, veterinaria=?, observaciones=?, eliminado=? WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getCodigo());
            ps.setDate(2, java.sql.Date.valueOf(m.getFechaImplantacion()));
            ps.setString(3, m.getVeterinaria());
            ps.setString(4, m.getObservaciones());
            ps.setBoolean(5, m.isEliminado());
            ps.setLong(6, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar microchip (transaccional)", e);
        }
    }

    /**
     * Eliminar un microchip usando una conexión externa.
     */
    @Override
    public void eliminar(long id, Connection c) {
        String sql = "UPDATE microchip SET eliminado = TRUE WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar microchip (transaccional)", e);
        }
    }

    /**
     * Leer un microchip usando una conexión externa.
     */
    @Override
    public Optional<Microchip> leer(long id, Connection c) {
        String sql = "SELECT * FROM microchip WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer microchip (transaccional)", e);
        }
        return Optional.empty();
    }

    /**
     * Listar mc usando una conexión externa.
     */
    @Override
    public List<Microchip> leerTodos(Connection c) {
        List<Microchip> lista = new ArrayList<>();
        String sql = "SELECT * FROM microchip AND eliminado = FALSE";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar microchips (transaccional)", e);
        }
        return lista;
    }

    /**
     * Recuperar un microchip usando una conexión externa.
     */
    public void recuperar(long id, Connection c) {
        String sql = "UPDATE microchip SET eliminado = FALSE WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar microchip (transaccional)", e);
        }
    }

    /**
     * Método auxiliar que convierte una fila del ResultSet en un objeto
     * Microchip. Este método centraliza el mapeo y evita duplicación de código.
     */
    private Microchip mapearResultSet(ResultSet rs) throws SQLException {
        Microchip m = new Microchip();

        m.setId(rs.getLong("id"));

        // eliminado: si es TINYINT(1) 0/1
        m.setEliminado(rs.getBoolean("eliminado"));

        m.setCodigo(rs.getString("codigo"));

        m.setFechaImplantacion(rs.getDate("fecha_implantacion").toLocalDate());

        m.setVeterinaria(rs.getString("veterinaria"));

        m.setObservaciones(rs.getString("observaciones"));

        return m;
    }

}
