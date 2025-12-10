/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortareas.utilidad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Luciano
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestor_tareas?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456"; //ponen su password de su MySQL

    // Singleton instance
    private static volatile DatabaseConnection instance;
    private Connection connection = null;

    private DatabaseConnection() {
        // Constructor privado
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Método mejorado para gestionar la conexión
    private synchronized Connection createConnectionIfNeeded() {
        try {
            // Verificar si la conexión es válida
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("Creando nueva conexión a la BD...");

                Class.forName("com.mysql.cj.jdbc.Driver");

                Properties properties = new Properties();
                properties.setProperty("user", USERNAME);
                properties.setProperty("password", PASSWORD);
                properties.setProperty("useSSL", "false");
                properties.setProperty("autoReconnect", "true");
                properties.setProperty("characterEncoding", "UTF-8");
                properties.setProperty("useUnicode", "true");
                properties.setProperty("serverTimezone", "America/Lima");
                properties.setProperty("tcpKeepAlive", "true"); // Nueva propiedad

                connection = DriverManager.getConnection(URL, properties);
                System.out.println("Conexión a la base de datos establecida correctamente");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL");
            e.printStackTrace();
            throw new RuntimeException("Driver MySQL no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            System.err.println("URL: " + URL);
            System.err.println("Usuario: " + USERNAME);
            System.err.println("Mensaje: " + e.getMessage());
            connection = null;
            throw new RuntimeException("Error al conectar con BD: " + e.getMessage(), e);
        }
        return connection;
    }

    // Método estático para obtener la conexión
    public static Connection getConnection() {
        return getInstance().createConnectionIfNeeded();
    }

    // Método para verificar si la conexión es válida
    public static boolean isConnectionValid() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    // Cierra la conexión en la instancia Singleton
    private synchronized void closeConnectionInternal() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Importante: establecer a null
                System.out.println("    Conexión a la base de datos cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // Método para forzar la reconexión
    public static void reconnect() {
        getInstance().closeConnectionInternal();
        System.out.println("Reconectando a la base de datos...");
        getInstance().createConnectionIfNeeded();
    }

    // Compatibilidad: cierre mediante llamada estática
    public static void closeConnection() {
        getInstance().closeConnectionInternal();
    }

    public static boolean testConnection() {
        return getInstance().testConnectionInternal();
    }

    private boolean testConnectionInternal() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            System.err.println("Test de conexión falló: " + e.getMessage());
            return false;
        }
    }
}
