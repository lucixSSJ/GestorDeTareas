package gestortareas.utilidad.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        System.out.println("Prueba de conexión con la base de datos");

        //prueba de conexión
        //testBasicConnection();

        //prueba de traer datos
        testQueryUsers();

    }

    private static void testBasicConnection(){
        System.out.println("\nPrueba Conexión");
        if (DatabaseConnection.testConnection()){
            System.out.println("Conexión exitosa");
        } else {
            System.out.println("No se conectó");
        }
    }

    private static void testQueryUsers(){
        System.out.println("Consultar usuarios existentes");
        String sql = "SELECT id_usuario, nombres, apellidos, email, username FROM usuarios";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("   Usuario " + count + ":");
                System.out.println("   ID: " + rs.getInt("id_usuario"));
                System.out.println("   Nombre: " + rs.getString("nombres") + " " + rs.getString("apellidos"));
                System.out.println("   Email: " + rs.getString("email"));
                System.out.println("   Username: " + rs.getString("username"));
                System.out.println("   ---");
            }

            if (count == 0) {
                System.out.println("No hay usuarios registrados en la base de datos");
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios: " + e.getMessage());
        }
    }

}
