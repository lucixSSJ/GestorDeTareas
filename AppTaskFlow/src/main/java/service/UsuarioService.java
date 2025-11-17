/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.UsuarioDao;
import dao.impl.JdbcUsuarioDao;
import domain.Usuario;
import utilidad.PasswordHasher;
import java.util.List;

/**
 * Servicio para la gestión de Usuarios y autenticación
 * @author Luciano
 */
public class UsuarioService {
    
    private final UsuarioDao usuarioDao;
    
    // Usuario actualmente logueado
    private static Usuario usuarioLogueado;
    
    public UsuarioService() {
        this.usuarioDao = JdbcUsuarioDao.getInstance();
    }
    
    /**
     * Registra un nuevo usuario
     */
    public boolean registrarUsuario(String nombres, String apellidos, String email, 
                                  String username, String password) {
        try {
            // Validaciones básicas
            if (!validarDatosRegistro(nombres, apellidos, email, username, password)) {
                return false;
            }
            
            // Verificar si ya existe el username o email
            if (usuarioDao.existeUsername(username)) {
                System.err.println("Error: El nombre de usuario '" + username + "' ya existe");
                return false;
            }
            
            if (usuarioDao.existeEmail(email)) {
                System.err.println("Error: El email '" + email + "' ya está registrado");
                return false;
            }
            
            // Crear y guardar el usuario
            String passwordHash = PasswordHasher.hashPassword(password);
            Usuario usuario = new Usuario(nombres.trim(), apellidos.trim(), 
                                        email.trim().toLowerCase(), username.trim(), passwordHash);
            
            boolean registrado = usuarioDao.guardar(usuario);
            
            if (registrado) {
                System.out.println("✅ Usuario '" + username + "' registrado exitosamente");
            } else {
                System.err.println("❌ Error al registrar el usuario '" + username + "'");
            }
            
            return registrado;
            
        } catch (Exception e) {
            System.err.println("Error inesperado al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Autentica un usuario (login)
     */
    public boolean login(String username, String password) {
        try {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                System.err.println("Error: Username y password son obligatorios");
                return false;
            }
            
            Usuario usuario = usuarioDao.autenticar(username.trim(), password);
            
            if (usuario != null) {
                usuarioLogueado = usuario;
                System.out.println("✅ Login exitoso: Bienvenido " + usuario.getNombreCompleto());
                return true;
            } else {
                System.err.println("❌ Credenciales incorrectas o usuario inactivo");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error inesperado en login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    public void logout() {
        if (usuarioLogueado != null) {
            System.out.println("👋 Sesión cerrada para: " + usuarioLogueado.getUsername());
            usuarioLogueado = null;
        }
    }
    
    /**
     * Obtiene el usuario actualmente logueado
     */
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
    
    /**
     * Verifica si hay un usuario logueado
     */
    public static boolean hayUsuarioLogueado() {
        return usuarioLogueado != null;
    }
    
    /**
     * Obtiene el ID del usuario logueado
     */
    public static int getIdUsuarioLogueado() {
        return usuarioLogueado != null ? usuarioLogueado.getIdUsuario() : -1;
    }
    
    /**
     * Actualiza los datos de un usuario
     */
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            if (usuario == null || usuario.getIdUsuario() <= 0) {
                System.err.println("Error: Usuario inválido para actualización");
                return false;
            }
            
            boolean actualizado = usuarioDao.actualizar(usuario);
            
            if (actualizado) {
                System.out.println("✅ Usuario ID " + usuario.getIdUsuario() + " actualizado exitosamente");
                
                // Si es el usuario logueado, actualizar la referencia
                if (usuarioLogueado != null && usuarioLogueado.getIdUsuario() == usuario.getIdUsuario()) {
                    usuarioLogueado = usuario;
                }
            } else {
                System.err.println("❌ Error al actualizar el usuario ID " + usuario.getIdUsuario());
            }
            
            return actualizado;
            
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cambia la contraseña de un usuario
     */
    public boolean cambiarPassword(int idUsuario, String passwordActual, String passwordNuevo) {
        try {
            Usuario usuario = usuarioDao.buscarPorId(idUsuario);
            if (usuario == null) {
                System.err.println("Error: Usuario no encontrado");
                return false;
            }
            
            // Verificar contraseña actual
            if (!PasswordHasher.verificarPassword(passwordActual, usuario.getPasswordHash())) {
                System.err.println("Error: Contraseña actual incorrecta");
                return false;
            }
            
            // Validar nueva contraseña
            if (passwordNuevo == null || passwordNuevo.length() < 6) {
                System.err.println("Error: La nueva contraseña debe tener al menos 6 caracteres");
                return false;
            }
            
            // Actualizar contraseña
            usuario.setPasswordHash(PasswordHasher.hashPassword(passwordNuevo));
            
            boolean actualizado = usuarioDao.actualizar(usuario);
            
            if (actualizado) {
                System.out.println("✅ Contraseña actualizada exitosamente");
            } else {
                System.err.println("❌ Error al actualizar la contraseña");
            }
            
            return actualizado;
            
        } catch (Exception e) {
            System.err.println("Error inesperado al cambiar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene un usuario por ID
     */
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioDao.buscarPorId(id);
    }
    
    /**
     * Obtiene un usuario por username
     */
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioDao.buscarPorUsername(username);
    }
    
    /**
     * Verifica si existe un username
     */
    public boolean existeUsername(String username) {
        return usuarioDao.existeUsername(username);
    }
    
    /**
     * Verifica si existe un email
     */
    public boolean existeEmail(String email) {
        return usuarioDao.existeEmail(email);
    }
    
    /**
     * Obtiene todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDao.obtenerTodos();
    }
    
    /**
     * Valida los datos para el registro de un usuario
     */
    private boolean validarDatosRegistro(String nombres, String apellidos, String email, 
                                       String username, String password) {
        
        if (nombres == null || nombres.trim().isEmpty()) {
            System.err.println("Error: El nombre es obligatorio");
            return false;
        }
        
        if (apellidos == null || apellidos.trim().isEmpty()) {
            System.err.println("Error: Los apellidos son obligatorios");
            return false;
        }
        
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Error: El email es obligatorio");
            return false;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            System.err.println("Error: El email no tiene un formato válido");
            return false;
        }
        
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Error: El nombre de usuario es obligatorio");
            return false;
        }
        
        if (username.length() < 3) {
            System.err.println("Error: El nombre de usuario debe tener al menos 3 caracteres");
            return false;
        }
        
        if (password == null || password.length() < 6) {
            System.err.println("Error: La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        return true;
    }
    
    /**
     * Crea el usuario administrador por defecto si no existe
     */
    public static void crearUsuarioAdminPorDefecto() {
        try {
            UsuarioDao dao = JdbcUsuarioDao.getInstance();
            
            // Verificar si ya existe el usuario admin
            Usuario adminExistente = dao.buscarPorUsername("admin");
            if (adminExistente != null) {
                System.out.println("✓ Usuario administrador ya existe");
                return;
            }
            
            // Crear usuario admin por defecto
            Usuario admin = new Usuario();
            admin.setNombres("Administrador");
            admin.setApellidos("Sistema");
            admin.setEmail("admin@taskflow.com");
            admin.setUsername("admin");
            admin.setPasswordHash(PasswordHasher.hashPassword("admin123"));
            admin.setFechaRegistro(java.time.LocalDateTime.now());
            admin.setUltimoLogin(java.time.LocalDateTime.now());
            
            boolean creado = dao.guardar(admin);
            
            if (creado) {
                System.out.println("✓ Usuario administrador creado exitosamente");
                System.out.println("  Usuario: admin");
                System.out.println("  Contraseña: admin123");
            } else {
                System.err.println("❌ Error al crear el usuario administrador");
            }
            
        } catch (Exception e) {
            System.err.println("Error al crear usuario administrador: " + e.getMessage());
            e.printStackTrace();
        }
    }
}