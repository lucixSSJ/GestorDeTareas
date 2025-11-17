/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.TareaDao;
import domain.Tarea;
import utilidad.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación híbrida de TareaDao - usa MySQL cuando está disponible, memoria como fallback
 * @author Luciano
 */
public class JdbcTareaDao implements TareaDao {
    
    // Instancia singleton
    private static JdbcTareaDao instance;
    
    // Fallback en memoria cuando no hay base de datos disponible
    private final List<Tarea> tareasMemoria = new ArrayList<>();
    private int siguienteId = 1;
    private boolean usarBaseDatos = true;
    
    private JdbcTareaDao() {
        // Constructor privado para patrón singleton
        // Verificar si se puede usar base de datos
        if (DatabaseConnection.testConnection()) {
            usarBaseDatos = true;
            crearTablasSiNoExisten();
            System.out.println("TareaDao inicializado en modo BASE DE DATOS");
        } else {
            usarBaseDatos = false;
            System.out.println("TareaDao inicializado en modo MEMORIA (sin base de datos)");
        }
    }
    
    public static synchronized JdbcTareaDao getInstance() {
        if (instance == null) {
            instance = new JdbcTareaDao();
        }
        return instance;
    }
    
    /**
     * Verifica que la tabla tareas exista (no la crea, ya existe en tu BD)
     */
    private void crearTablasSiNoExisten() {
        String verificarSQL = "SELECT COUNT(*) FROM tareas";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("No hay conexión disponible");
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(verificarSQL)) {
                
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("✅ Tabla 'tareas' verificada - contiene " + count + " registros");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar tabla de tareas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean guardar(Tarea tarea) {
        if (tarea == null) return false;
        
        if (!usarBaseDatos) {
            // Modo memoria
            tarea.setId(siguienteId++);
            tareasMemoria.add(tarea);
            System.out.println("Tarea guardada en memoria con ID: " + tarea.getId());
            return true;
        }
        
        // Modo base de datos - adaptado a tu estructura
        String sql = """
            INSERT INTO tareas (id_usuario, nombre_tarea, descripcion, fecha_limite, estado, prioridad, fecha_creacion) 
            VALUES (?, ?, ?, ?, ?, ?, NOW())
            """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                // Fallback a memoria si la conexión falla
                return guardarEnMemoria(tarea);
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Por ahora usar id_usuario = 1 (el primer usuario de tu BD)
            pstmt.setInt(1, 1);
            pstmt.setString(2, tarea.getTitulo());
            pstmt.setString(3, tarea.getDescripcion());
            
            // Fecha límite (requerida en tu BD)
            if (tarea.getFechaVencimiento() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(tarea.getFechaVencimiento().atStartOfDay()));
            } else {
                // Si no hay fecha, usar mañana por defecto
                pstmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now().plusDays(1)));
            }
            
            pstmt.setString(5, tarea.getEstado() != null ? tarea.getEstado().toLowerCase() : "pendiente");
            pstmt.setString(6, tarea.getPrioridad() != null ? tarea.getPrioridad().toLowerCase() : "media");
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tarea.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Tarea guardada en base de datos con ID: " + tarea.getId());
                return true;
            }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al guardar tarea en base de datos, usando memoria: " + e.getMessage());
            return guardarEnMemoria(tarea);
        }
        return false;
    }
    
    /**
     * Método auxiliar para guardar en memoria
     */
    private boolean guardarEnMemoria(Tarea tarea) {
        tarea.setId(siguienteId++);
        tareasMemoria.add(tarea);
        System.out.println("Tarea guardada en memoria con ID: " + tarea.getId());
        return true;
    }
    
    @Override
    public boolean actualizar(Tarea tarea) {
        if (tarea == null || tarea.getId() <= 0) return false;
        
        if (!usarBaseDatos) {
            // Modo memoria - buscar y actualizar en lista
            for (int i = 0; i < tareasMemoria.size(); i++) {
                if (tareasMemoria.get(i).getId() == tarea.getId()) {
                    tareasMemoria.set(i, tarea);
                    System.out.println("Tarea actualizada en memoria: ID " + tarea.getId());
                    return true;
                }
            }
            return false;
        }
        
        // Modo base de datos - adaptado a tu estructura
        String sql = """
            UPDATE tareas 
            SET nombre_tarea = ?, descripcion = ?, prioridad = ?, estado = ?, fecha_limite = ?
            WHERE id_tarea = ?
            """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, tarea.getTitulo());
                pstmt.setString(2, tarea.getDescripcion());
                pstmt.setString(3, tarea.getPrioridad() != null ? tarea.getPrioridad().toLowerCase() : "media");
                pstmt.setString(4, tarea.getEstado() != null ? tarea.getEstado().toLowerCase() : "pendiente");
                
                // Fecha límite
                if (tarea.getFechaVencimiento() != null) {
                    pstmt.setTimestamp(5, Timestamp.valueOf(tarea.getFechaVencimiento().atStartOfDay()));
                } else {
                    pstmt.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now().plusDays(1)));
                }
                
                pstmt.setInt(6, tarea.getId());
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Tarea actualizada exitosamente: ID " + tarea.getId());
                    return true;
                } else {
                    System.out.println("No se encontró tarea con ID: " + tarea.getId());
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean eliminar(int id) {
        if (id <= 0) return false;
        
        if (!usarBaseDatos) {
            // Modo memoria - buscar y eliminar de lista
            for (int i = 0; i < tareasMemoria.size(); i++) {
                if (tareasMemoria.get(i).getId() == id) {
                    tareasMemoria.remove(i);
                    System.out.println("Tarea eliminada de memoria: ID " + id);
                    return true;
                }
            }
            return false;
        }
        
        // Modo base de datos - usar nombre correcto de columna
        String sql = "DELETE FROM tareas WHERE id_tarea = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Tarea eliminada exitosamente: ID " + id);
                    return true;
                } else {
                    System.out.println("No se encontró tarea con ID: " + id);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public Tarea buscarPorId(int id) {
        if (id <= 0) return null;
        
        if (!usarBaseDatos) {
            // Modo memoria - buscar en lista
            return tareasMemoria.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
        
        // Modo base de datos - usar nombre correcto de columna
        String sql = "SELECT * FROM tareas WHERE id_tarea = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearResultSetATarea(rs);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar tarea por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Tarea> obtenerTodas() {
        if (!usarBaseDatos) {
            // Modo memoria
            return new ArrayList<>(tareasMemoria);
        }
        
        // Modo base de datos - adaptado a tu estructura
        String sql = "SELECT * FROM tareas ORDER BY fecha_creacion DESC";
        List<Tarea> tareas = new ArrayList<>();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                // Fallback a memoria
                return new ArrayList<>(tareasMemoria);
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    tareas.add(mapearResultSetATarea(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las tareas de base de datos, usando memoria: " + e.getMessage());
            return new ArrayList<>(tareasMemoria);
        }
        return tareas;
    }
    
    @Override
    public List<Tarea> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM tareas WHERE id_categoria = ? ORDER BY fecha_creacion DESC";
        List<Tarea> tareas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categoria);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearResultSetATarea(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas por categoría: " + e.getMessage());
            e.printStackTrace();
        }
        return tareas;
    }
    
    @Override
    public List<Tarea> buscarPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM tareas WHERE estado = ? ORDER BY fecha_creacion DESC";
        List<Tarea> tareas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearResultSetATarea(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas por estado: " + e.getMessage());
            e.printStackTrace();
        }
        return tareas;
    }
    
    @Override
    public List<Tarea> buscarPorPrioridad(String prioridad) {
        if (prioridad == null || prioridad.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM tareas WHERE prioridad = ? ORDER BY fecha_creacion DESC";
        List<Tarea> tareas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prioridad);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearResultSetATarea(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas por prioridad: " + e.getMessage());
            e.printStackTrace();
        }
        return tareas;
    }
    
    /**
     * Método adicional para obtener el conteo total de tareas
     * @return número total de tareas
     */
    public int obtenerConteoTotal() {
        if (!usarBaseDatos) {
            return tareasMemoria.size();
        }
        
        String sql = "SELECT COUNT(*) as total FROM tareas";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return tareasMemoria.size();
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener conteo de tareas: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Método auxiliar para mapear ResultSet a objeto Tarea (adaptado a tu BD)
     */
    private Tarea mapearResultSetATarea(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        
        // Mapear campos de tu base de datos existente
        tarea.setId(rs.getInt("id_tarea"));
        tarea.setTitulo(rs.getString("nombre_tarea"));
        tarea.setDescripcion(rs.getString("descripcion"));
        
        // Mapear categoria por ID (por ahora como string)
        int idCategoria = rs.getInt("id_categoria");
        if (!rs.wasNull()) {
            tarea.setCategoria("Categoría " + idCategoria);
        }
        
        // Mapear prioridad y estado
        String prioridad = rs.getString("prioridad");
        String estado = rs.getString("estado");
        
        tarea.setPrioridad(prioridad != null ? prioridad.toUpperCase() : "MEDIA");
        tarea.setEstado(estado != null ? estado.toUpperCase() : "PENDIENTE");
        
        // Manejar fecha límite
        Timestamp fechaLimite = rs.getTimestamp("fecha_limite");
        if (fechaLimite != null) {
            tarea.setFechaVencimiento(fechaLimite.toLocalDateTime().toLocalDate());
        }
        
        return tarea;
    }
    
    /**
     * Método adicional para limpiar todas las tareas (útil para testing)
     */
    public void limpiarTodas() {
        if (!usarBaseDatos) {
            tareasMemoria.clear();
            System.out.println("Todas las tareas eliminadas de memoria");
            return;
        }
        
        String sql = "DELETE FROM tareas";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                tareasMemoria.clear();
                return;
            }
            
            try (Statement stmt = conn.createStatement()) {
                int filasAfectadas = stmt.executeUpdate(sql);
                System.out.println("Se eliminaron " + filasAfectadas + " tareas de la base de datos");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al limpiar todas las tareas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
