/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.UsuarioDao;
import domain.Usuario;
import utilidad.DatabaseConnection;
import utilidad.PasswordHasher;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Usuario usando JDBC
 * @author Luciano
 */
public class JdbcUsuarioDao implements UsuarioDao {
    
    private static JdbcUsuarioDao instance;
    private List<Usuario> usuariosMemoria; // Fallback para cuando no hay BD
    private boolean usarBaseDatos;
    private int siguienteId = 1;

    private JdbcUsuarioDao() {
        usuariosMemoria = new ArrayList<>();
        // Verificar si podemos conectar a la base de datos
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                usarBaseDatos = true;
                verificarTabla();
                System.out.println("✅ UsuarioDao conectado a base de datos 'gestor_tareas'");
            } else {
                usarBaseDatos = false;
                inicializarUsuarioDefecto();
                System.out.println("⚠ UsuarioDao en modo MEMORIA - instala driver MySQL para usar BD");
            }
        } catch (Exception e) {
            usarBaseDatos = false;
            inicializarUsuarioDefecto();
            System.out.println("⚠ UsuarioDao en modo MEMORIA - error: " + e.getMessage());
        }
    }

    public static synchronized JdbcUsuarioDao getInstance() {
        if (instance == null) {
            instance = new JdbcUsuarioDao();
        }
        return instance;
    }
    
    private void verificarTabla() {
        String verificarSQL = "SELECT COUNT(*) FROM usuarios";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("No hay conexión disponible");
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(verificarSQL)) {
                
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("✅ Tabla 'usuarios' verificada - contiene " + count + " registros");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar tabla de usuarios: " + e.getMessage());
        }
    }
    
    private void inicializarUsuarioDefecto() {
        // Crear usuario administrador por defecto
        Usuario admin = new Usuario("Administrador", "Sistema", "admin@taskflow.com", "admin", 
                                   PasswordHasher.hashPassword("admin123"));
        admin.setIdUsuario(1);
        admin.setFechaRegistro(LocalDateTime.now());
        usuariosMemoria.add(admin);
        
        siguienteId = 2;
        System.out.println("👤 Usuario administrador creado: username='admin', password='admin123'");
    }

    @Override
    public boolean guardar(Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPasswordHash() == null) {
            return false;
        }
        
        if (!usarBaseDatos) {
            // Modo memoria
            usuario.setIdUsuario(siguienteId++);
            usuario.setFechaRegistro(LocalDateTime.now());
            usuariosMemoria.add(usuario);
            System.out.println("Usuario guardado en memoria con ID: " + usuario.getIdUsuario());
            return true;
        }
        
        // Modo base de datos
        String sql = """
            INSERT INTO usuarios (nombres, apellidos, email, username, password_hash, 
                                 fecha_registro, notificaciones_vencimiento, dias_antes_vencimiento, activo) 
            VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, ?)
            """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                return guardarEnMemoria(usuario);
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, usuario.getNombres());
                pstmt.setString(2, usuario.getApellidos());
                pstmt.setString(3, usuario.getEmail());
                pstmt.setString(4, usuario.getUsername());
                pstmt.setString(5, usuario.getPasswordHash());
                pstmt.setBoolean(6, usuario.isNotificacionesVencimiento());
                pstmt.setInt(7, usuario.getDiasAntesVencimiento());
                pstmt.setBoolean(8, usuario.isActivo());
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            usuario.setIdUsuario(generatedKeys.getInt(1));
                        }
                    }
                    System.out.println("Usuario guardado en base de datos con ID: " + usuario.getIdUsuario());
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario en base de datos, usando memoria: " + e.getMessage());
            return guardarEnMemoria(usuario);
        }
        return false;
    }
    
    private boolean guardarEnMemoria(Usuario usuario) {
        usuario.setIdUsuario(siguienteId++);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuariosMemoria.add(usuario);
        System.out.println("Usuario guardado en memoria con ID: " + usuario.getIdUsuario());
        return true;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() <= 0) {
            return false;
        }
        
        if (!usarBaseDatos) {
            // Modo memoria
            for (int i = 0; i < usuariosMemoria.size(); i++) {
                if (usuariosMemoria.get(i).getIdUsuario() == usuario.getIdUsuario()) {
                    usuariosMemoria.set(i, usuario);
                    System.out.println("Usuario actualizado en memoria: ID " + usuario.getIdUsuario());
                    return true;
                }
            }
            return false;
        }
        
        // Modo base de datos
        String sql = """
            UPDATE usuarios 
            SET nombres = ?, apellidos = ?, email = ?, username = ?, 
                notificaciones_vencimiento = ?, dias_antes_vencimiento = ?, activo = ?
            WHERE id_usuario = ?
            """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, usuario.getNombres());
                pstmt.setString(2, usuario.getApellidos());
                pstmt.setString(3, usuario.getEmail());
                pstmt.setString(4, usuario.getUsername());
                pstmt.setBoolean(5, usuario.isNotificacionesVencimiento());
                pstmt.setInt(6, usuario.getDiasAntesVencimiento());
                pstmt.setBoolean(7, usuario.isActivo());
                pstmt.setInt(8, usuario.getIdUsuario());
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Usuario actualizado exitosamente: ID " + usuario.getIdUsuario());
                    return true;
                } else {
                    System.out.println("No se encontró usuario con ID: " + usuario.getIdUsuario());
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        if (id <= 0) return false;
        
        if (!usarBaseDatos) {
            // Modo memoria
            for (int i = 0; i < usuariosMemoria.size(); i++) {
                if (usuariosMemoria.get(i).getIdUsuario() == id) {
                    usuariosMemoria.remove(i);
                    System.out.println("Usuario eliminado de memoria: ID " + id);
                    return true;
                }
            }
            return false;
        }
        
        // Eliminar de base de datos
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Usuario eliminado exitosamente: ID " + id);
                    return true;
                } else {
                    System.out.println("No se encontró usuario con ID: " + id);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Usuario buscarPorId(int id) {
        if (id <= 0) return null;
        
        if (!usarBaseDatos) {
            return usuariosMemoria.stream()
                    .filter(u -> u.getIdUsuario() == id)
                    .findFirst()
                    .orElse(null);
        }
        
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearResultSetAUsuario(rs);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        
        if (!usarBaseDatos) {
            return usuariosMemoria.stream()
                    .filter(u -> username.equals(u.getUsername()))
                    .findFirst()
                    .orElse(null);
        }
        
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, username);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearResultSetAUsuario(rs);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por username: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) return null;
        
        if (!usarBaseDatos) {
            return usuariosMemoria.stream()
                    .filter(u -> email.equals(u.getEmail()))
                    .findFirst()
                    .orElse(null);
        }
        
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, email);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearResultSetAUsuario(rs);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean existeUsername(String username) {
        return buscarPorUsername(username) != null;
    }

    @Override
    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }

    @Override
    public Usuario autenticar(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        Usuario usuario = buscarPorUsername(username.trim());
        
        if (usuario != null && usuario.isActivo()) {
            if (PasswordHasher.verificarPassword(password, usuario.getPasswordHash())) {
                // Actualizar último login
                actualizarUltimoLogin(usuario.getIdUsuario());
                usuario.setUltimoLogin(LocalDateTime.now());
                return usuario;
            }
        }
        
        return null;
    }

    @Override
    public boolean actualizarUltimoLogin(int idUsuario) {
        if (!usarBaseDatos) {
            // En memoria, ya se actualiza en autenticar()
            return true;
        }
        
        String sql = "UPDATE usuarios SET ultimo_login = NOW() WHERE id_usuario = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                return pstmt.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar último login: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        if (!usarBaseDatos) {
            return new ArrayList<>(usuariosMemoria);
        }
        
        String sql = "SELECT * FROM usuarios ORDER BY fecha_registro DESC";
        List<Usuario> usuarios = new ArrayList<>();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                return new ArrayList<>(usuariosMemoria);
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    usuarios.add(mapearResultSetAUsuario(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios de base de datos, usando memoria: " + e.getMessage());
            return new ArrayList<>(usuariosMemoria);
        }
        return usuarios;
    }
    
    /**
     * Mapea un ResultSet a objeto Usuario
     */
    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        
        // Manejar fechas
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            usuario.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        Timestamp ultimoLogin = rs.getTimestamp("ultimo_login");
        if (ultimoLogin != null) {
            usuario.setUltimoLogin(ultimoLogin.toLocalDateTime());
        }
        
        usuario.setNotificacionesVencimiento(rs.getBoolean("notificaciones_vencimiento"));
        usuario.setDiasAntesVencimiento(rs.getInt("dias_antes_vencimiento"));
        usuario.setActivo(rs.getBoolean("activo"));
        
        return usuario;
    }
}