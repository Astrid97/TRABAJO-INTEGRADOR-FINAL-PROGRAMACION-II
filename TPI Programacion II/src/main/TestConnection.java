package main;


import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class TestConnection {

       public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexion establecida con exito.");
 
            // 🔹 Crear y ejecutar consulta SQL con PreparedStatement
                String sql = "SELECT * FROM mascota";
                try (PreparedStatement pstmt = conn.prepareStatement(sql); 
                        ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("Listado de mascotas:");
                    while (rs.next()) {

                        String nombre = rs.getString("nombre");

                        System.out.println(" Nombre: " + nombre);
                    }
                }
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            // 🔹 Manejo de errores en la conexión a la base de datos
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para depuración
        }
    }

}