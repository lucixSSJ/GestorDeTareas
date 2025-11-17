package utilidad;

import java.sql.*;

public class VerificarEstructuraTabla {
    
    public static void main(String[] args) {
        System.out.println("Verificando estructura de la tabla 'tareas'...");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("No se pudo conectar a la base de datos");
                return;
            }
            
            // Verificar si la tabla existe y su estructura
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "tareas", null);
            
            if (tables.next()) {
                System.out.println("✅ Tabla 'tareas' encontrada");
                
                // Obtener columnas
                ResultSet columns = metaData.getColumns(null, null, "tareas", null);
                System.out.println("\nColumnas de la tabla 'tareas':");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    String isNullable = columns.getString("IS_NULLABLE");
                    System.out.println("  - " + columnName + " (" + dataType + "(" + columnSize + ")) " + 
                                     (isNullable.equals("YES") ? "NULL" : "NOT NULL"));
                }
                
                // Mostrar algunas filas de ejemplo
                System.out.println("\nContenido actual de la tabla:");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM tareas LIMIT 5");
                
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                // Mostrar nombres de columnas
                System.out.print("Columnas: ");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + " ");
                }
                System.out.println();
                
                // Mostrar datos
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.print("Fila " + count + ": ");
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs.getString(i) + " | ");
                    }
                    System.out.println();
                }
                
                if (count == 0) {
                    System.out.println("La tabla está vacía");
                }
                
            } else {
                System.out.println("❌ Tabla 'tareas' NO encontrada");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar estructura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}