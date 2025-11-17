package utilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertarTareasPrueba {
    
    public static void main(String[] args) {
        System.out.println("Insertando tareas de prueba en la base de datos...");
        
        insertarTareaPrueba("Completar proyecto Java", "Finalizar el gestor de tareas con conexión a MySQL", "Trabajo", "ALTA", "EN_PROGRESO", "2024-12-01");
        insertarTareaPrueba("Estudiar base de datos", "Repasar conceptos de MySQL y JDBC", "Estudio", "MEDIA", "PENDIENTE", "2024-11-30");
        insertarTareaPrueba("Reunión equipo", "Discutir avances del proyecto", "Trabajo", "ALTA", "PENDIENTE", "2024-11-20");
        insertarTareaPrueba("Ejercicio diario", "Hacer 30 minutos de ejercicio", "Personal", "BAJA", "COMPLETADA", null);
        
        System.out.println("¡Tareas de prueba insertadas exitosamente!");
    }
    
    private static void insertarTareaPrueba(String titulo, String descripcion, String categoria, 
                                          String prioridad, String estado, String fechaVencimiento) {
        String sql = """
            INSERT INTO tareas (titulo, descripcion, categoria, prioridad, estado, fecha_vencimiento) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("No se pudo conectar a la base de datos");
                return;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, titulo);
                pstmt.setString(2, descripcion);
                pstmt.setString(3, categoria);
                pstmt.setString(4, prioridad);
                pstmt.setString(5, estado);
                
                if (fechaVencimiento != null) {
                    pstmt.setDate(6, java.sql.Date.valueOf(fechaVencimiento));
                } else {
                    pstmt.setNull(6, java.sql.Types.DATE);
                }
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("✅ Tarea insertada: " + titulo);
                } else {
                    System.out.println("❌ Error al insertar: " + titulo);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar tarea '" + titulo + "': " + e.getMessage());
        }
    }
}