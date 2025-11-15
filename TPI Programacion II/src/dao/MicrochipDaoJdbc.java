package dao;

import config.DatabaseConnection;
import entities.Microchip;
import java.sql.*;
import java.util.*;

public class MicrochipDaoJdbc implements GenericDao<Microchip> {

    @Override
    public Microchip crear(Microchip m) {
        try (Connection c = DatabaseConnection.getConnection()) {
            return crear(m, c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear microchip", e);
        }
    }

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

    @Override
    public Optional<Microchip> leer(long id) {
        String sql = "SELECT * FROM microchip WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Microchip m = mapearResultSet(rs);
                    return Optional.of(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer microchip", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Microchip> leerTodos() {
        List<Microchip> lista = new ArrayList<>();
        String sql = "SELECT * FROM microchip";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar microchips", e);
        }
        return lista;
    }

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

    public void recuperar(long id) {
        String sql = "UPDATE microchip SET eliminado = FALSE WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar microchip", e);
        }
    }

    // Métodos con Connection
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

    @Override
    public Optional<Microchip> leer(long id, Connection c) {
        String sql = "SELECT * FROM microchip WHERE id = ?";
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

    @Override
    public List<Microchip> leerTodos(Connection c) {
        List<Microchip> lista = new ArrayList<>();
        String sql = "SELECT * FROM microchip";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar microchips (transaccional)", e);
        }
        return lista;
    }

    public void recuperar(long id, Connection c) {
        String sql = "UPDATE microchip SET eliminado = FALSE WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar microchip (transaccional)", e);
        }
    }

    // Método auxiliar para mapear ResultSet a objeto
    private Microchip mapearResultSet(ResultSet rs) throws SQLException {
        Microchip m = new Microchip();
        m.setId(rs.getLong("id"));
        m.setCodigo(rs.getString("codigo"));
        m.setFechaImplantacion(rs.getDate("fecha_implantacion").toLocalDate());
        m.setVeterinaria(rs.getString("veterinaria"));
        m.setObservaciones(rs.getString("observaciones"));
        m.setEliminado(rs.getBoolean("eliminado"));
        return m;
    }
}
