package gestortareas.utilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gestortareas.utilidad.db.DatabaseConnection;
import gestortareas.utilidad.passwordhasher.PasswordHasher;

public class MigradorPassword {
    /**
    public static void migrarPasswordsExistentes() {
        String selectSql = "SELECT id_usuario, password_hash FROM usuarios WHERE LENGTH(password_hash) < 50";
        String updateSql = "UPDATE usuarios SET password_hash = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             ResultSet rs = selectStmt.executeQuery()) {

            int migrados = 0;
            while (rs.next()) {
                int usuarioId = rs.getInt("id_usuario");
                String passwordPlana = rs.getString("password_hash");

                // Hashear la contraseña en texto plano
                String passwordHash = PasswordHasher.hashPassword(passwordPlana);

                // Actualizar en la BD
                updateStmt.setString(1, passwordHash);
                updateStmt.setInt(2, usuarioId);
                updateStmt.executeUpdate();

                migrados++;
                System.out.println("Migrado usuario ID: " + usuarioId);
            }

            System.out.println("Migración completada: " + migrados + " usuarios actualizados");

        } catch (SQLException e) {
            System.err.println("Error en migración: " + e.getMessage());
        }
    }
    */


    // Ejecutar una vez para corregir las contraseñas
    /**
    public static void rehashearTodasLasContraseñas() {
        String selectSql = "SELECT id_usuario, password_hash FROM usuarios";
        String updateSql = "UPDATE usuarios SET password_hash = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             ResultSet rs = selectStmt.executeQuery()) {

            int actualizadas = 0;
            while (rs.next()) {
                int usuarioId = rs.getInt("id_usuario");
                String passwordActual = rs.getString("password_hash");

                // Si la contraseña parece estar en texto plano o con hash viejo
                if (passwordActual != null && passwordActual.length() < 50) {
                    String nuevoHash = PasswordHasher.hashPassword(passwordActual);

                    updateStmt.setString(1, nuevoHash);
                    updateStmt.setInt(2, usuarioId);
                    updateStmt.executeUpdate();

                    System.out.println("   Re-hasheado usuario ID: " + usuarioId);
                    System.out.println("   Contraseña anterior: " + passwordActual);
                    System.out.println("   Nuevo hash: " + nuevoHash);
                    actualizadas++;
                }
            }

            System.out.println("Re-hasheo completado: " + actualizadas + " usuarios actualizados");

        } catch (SQLException e) {
            System.err.println("Error en re-hasheo: " + e.getMessage());
        }
    }
    */


    /**
    public static void reiniciarSistemaCompleto() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Limpiar tablas relacionadas primero (por las foreign keys)
            stmt.executeUpdate("DELETE FROM tareas");
            stmt.executeUpdate("DELETE FROM categorias");
            stmt.executeUpdate("DELETE FROM usuarios");

            // 2. Crear usuarios con hashes CORRECTOS
            String hashLuciano = PasswordHasher.hashPassword("luciano");
            String hashPrueba = PasswordHasher.hashPassword("prueba");

            String insertSql = "INSERT INTO usuarios (nombres, apellidos, email, username, password_hash) VALUES " +
                    "('Luciano', 'Bances', 'luciano@gmail.com', 'luciano', '" + hashLuciano + "'), " +
                    "('María', 'Gómez', 'maria.gomez@email.com', 'mariagomez', '" + hashPrueba + "')";

            stmt.executeUpdate(insertSql);

            System.out.println("Sistema reiniciado correctamente");
            System.out.println("Hash luciano: " + hashLuciano);
            System.out.println("Hash prueba: " + hashPrueba);

        } catch (SQLException e) {
            System.err.println("Error reiniciando sistema: " + e.getMessage());
        }
    }
     */


    /**
    public static void verificarTodosLosHashes() {
        System.out.println("HASHES CORRECTOS:");
        System.out.println("luciano: " + PasswordHasher.hashPassword("luciano"));
        System.out.println("prueba: " + PasswordHasher.hashPassword("prueba"));
        System.out.println("test123: " + PasswordHasher.hashPassword("test123"));
        System.out.println("password: " + PasswordHasher.hashPassword("password"));
    }
     */


    /**
    public static void pruebaUsuarioNose() {
        String passwordFromUser = "luciano"; // Lo que el usuario ingresa
        String storedHashForNose = "6T2WjEGtcyUX9XgAYhjwAcScJ13W7X0RD6Tc+i4kR+g="; // Hash en BD

        System.out.println("PRUEBA USUARIO 'nose':");
        System.out.println("Contraseña ingresada: '" + passwordFromUser + "'");
        System.out.println("Hash almacenado: " + storedHashForNose);

        boolean resultado = PasswordHasher.verificarPassword(passwordFromUser, storedHashForNose);
        System.out.println("Resultado verificación: " + resultado);
    }
    */


    // ejecutar para pruebas de fallas
    public static void main(String[] args) {
        //migrarPasswordsExistentes();
        //rehashearTodasLasContraseñas();
        //reiniciarSistemaCompleto();
        //String nuevoHash = PasswordHasher.hashPassword("luciano");
        //System.out.println(nuevoHash);
        //PasswordHasher.testConsistencia();
        //verificarTodosLosHashes();
        //pruebaUsuarioNose();
    }
}
