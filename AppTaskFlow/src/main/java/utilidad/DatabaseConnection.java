/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Luciano
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestor_tareas";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2000"; //ponene su contraseña del MySQL

    private static Connection connection = null;

    public static Connection getConnection(){
        if (connection == null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Properties properties = new Properties();
                properties.setProperty("user", USERNAME);
                properties.setProperty("password", PASSWORD);
                properties.setProperty("useSSL", "false");
                properties.setProperty("autoReconnect", "true");
                properties.setProperty("characterEncoding", "UTF-8");
                properties.setProperty("useUnicode", "true");
                properties.setProperty("serverTimezone", "UTC");

                connection = DriverManager.getConnection(URL, properties);
                System.out.println("Conexión a la base de datos establecida correctamente");
            } catch (ClassNotFoundException e){
                System.err.println("Error: No se encontró el driver de MySQL");
                e.printStackTrace();
            } catch (SQLException e){
                System.err.println("Error al conectar con la base de datos:");
                System.err.println("URL: " + URL);
                System.err.println("Usuario: " + USERNAME);
                System.err.println("Mensaje: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection(){
        if (connection != null){
            try {
                connection.close();
                connection = null;
                System.out.println("Conección a la base de datos cerrada");
            }catch (SQLException e){
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection(){
        try (Connection conn = getConnection()){
            return conn != null && !conn.isClosed();
        }catch (SQLException e){
            return false;
        }
    }

}
